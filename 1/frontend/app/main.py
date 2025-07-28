import streamlit as st
import pandas as pd
import requests
import plotly.express as px
import matplotlib.pyplot as plt
import seaborn as sns

st.set_page_config(layout="wide", page_title="NASDAQ Dashboard")

API_BASE = "http://localhost:8000"

# Sidebar - select page
page = st.sidebar.radio("Sections", [
    "Meta Basic Analysis",
    "Meta Visualization",
    "Meta Adv. Analysis",
    "Stock Price Data",
    "Stock Price Visualization"
])

@st.cache_data
def get_all_stocks():
    return pd.DataFrame(requests.get(f"{API_BASE}/stocks").json())

@st.cache_data
def get_categories_dist():
    return requests.get(f"{API_BASE}/distribution/categories").json()

@st.cache_data
def get_exchanges_dist():
    return requests.get(f"{API_BASE}/distribution/exchanges").json()

@st.cache_data
def get_etf_dist():
    return requests.get(f"{API_BASE}/distribution/etf").json()

@st.cache_data
def get_stock_metadata(symbol):
    return requests.get(f"{API_BASE}/stocks/{symbol}").json()

@st.cache_data
def get_stock_price_data(symbol):
    df = pd.DataFrame(requests.get(f"{API_BASE}/stocks/{symbol}/data").json())
    df["Date"] = pd.to_datetime(df["Date"])
    df.set_index("Date", inplace=True)
    return df.sort_index()

# --- PAGE 1: Meta Basic Analysis---
if page == "Meta Basic Analysis":
    st.title("Meta Basic Analysis")

    df_meta = get_all_stocks()

    # Unique stocks count
    unique_stocks_count = df_meta["Symbol"].nunique()
    st.metric("Unique NASDAQ Stocks", unique_stocks_count)

    # Distribution of stocks by Listing Exchange
    exchanges = df_meta["Listing Exchange"].value_counts()
    st.subheader("Distribution of Stocks Across Listing Exchanges")
    st.dataframe(exchanges)

    # Number of ETFs
    etf_dist = get_etf_dist()
    etf_count = etf_dist.get("Y", 0)
    st.metric("Number of ETFs", etf_count)

# --- PAGE 2: Meta Visualization ---
elif page == "Meta Visualization":
    st.title("Meta Visualization")

    df_meta = get_all_stocks()

    # Bar chart: number of stocks per Listing Exchange
    st.subheader("Stocks per Listing Exchange")
    exchanges = df_meta["Listing Exchange"].value_counts()
    fig_bar = px.bar(x=exchanges.index, y=exchanges.values,
                     labels={"x": "Listing Exchange", "y": "Count"},
                     title="Stocks per Listing Exchange")
    st.plotly_chart(fig_bar, use_container_width=True)

    # Pie chart: market categories distribution
    st.subheader("Market Categories Distribution")
    categories = get_categories_dist()
    fig_pie = px.pie(names=list(categories.keys()), values=list(categories.values()),
                     title="Market Category Distribution")
    st.plotly_chart(fig_pie, use_container_width=True)

    # Scatter plot: round lot size vs symbol (top 10)
    st.subheader("Top 10 Companies by Round Lot Size")
    if "Round Lot Size" in df_meta.columns:
        df_meta["Round Lot Size"] = pd.to_numeric(df_meta["Round Lot Size"], errors="coerce")
        top10 = df_meta.sort_values("Round Lot Size", ascending=False).head(10)
        fig_scatter = px.scatter(top10, x="Symbol", y="Round Lot Size", size="Round Lot Size", color="Symbol",
                                 title="Round Lot Size vs Symbol (Top 10)")
        st.plotly_chart(fig_scatter, use_container_width=True)
    else:
        st.warning("Round Lot Size column not found in metadata.")

# --- PAGE 3: Meta Adv. Analysis ---
elif page == "Meta Adv. Analysis":
    st.title("Meta Adv. Analysis")

    df_meta = get_all_stocks()

    # Proportion ETFs vs non-ETFs
    etf_dist = get_etf_dist()
    etf_counts = [etf_dist.get("Y", 0), etf_dist.get("N", 0)]
    labels = ["ETFs", "Non-ETFs"]
    st.subheader("ETFs vs Non-ETFs Proportion")
    fig_etf = px.pie(names=labels, values=etf_counts, title="ETF vs Non-ETF Distribution")
    st.plotly_chart(fig_etf, use_container_width=True)

    # Top 10 companies by round lot size
    st.subheader("Top 10 Companies by Round Lot Size")
    if "Round Lot Size" in df_meta.columns:
        df_meta["Round Lot Size"] = pd.to_numeric(df_meta["Round Lot Size"], errors="coerce")
        top10 = df_meta.sort_values("Round Lot Size", ascending=False).head(10)
        st.table(top10[["Symbol", "Security Name", "Round Lot Size"]])

        # Scatter plot (repeated from Visualization for clarity here)
        st.subheader("Scatter Plot: Round Lot Size vs Symbol")
        fig_scatter = px.scatter(top10, x="Symbol", y="Round Lot Size", size="Round Lot Size", color="Symbol")
        st.plotly_chart(fig_scatter, use_container_width=True)
    else:
        st.warning("Round Lot Size column not found in metadata.")

    # Correlation matrix
    st.subheader("Correlation Matrix (Numerical Columns)")
    num_df = df_meta.select_dtypes(include=["float64", "int64"]).dropna(axis=1, how="any")

    if not num_df.empty:
        corr = num_df.corr()
        # Handle even single-column case by forcing a dataframe with 1x1 matrix
        if corr.shape[0] == 1 and corr.shape[1] == 1:
            # corr is a 1x1 DataFrame with value 1.0 by definition
            fig_corr, ax = plt.subplots(figsize=(3, 3))
            sns.heatmap(corr, annot=True, cmap="coolwarm", cbar=False, square=True, ax=ax)
            ax.set_title("Correlation Matrix")
            st.pyplot(fig_corr)
        else:
            fig_corr, ax = plt.subplots(figsize=(10, 6))
            sns.heatmap(corr, annot=True, cmap="coolwarm", ax=ax)
            ax.set_title("Correlation Matrix")
            st.pyplot(fig_corr)
    else:
        st.info("No numeric columns found for correlation matrix.")


# --- PAGE 4: Stock Price Data ---
elif page == "Stock Price Data":
    st.title("Stock Price Data")

    df_meta = get_all_stocks()
    stock_symbols = df_meta["Symbol"].sort_values().unique()
    selected_symbol = st.selectbox("Select Stock Symbol for Analysis", stock_symbols)

    if selected_symbol:
        # Fetch and display metadata for the selected symbol
        meta = get_stock_metadata(selected_symbol)
        st.subheader(f"Meta for {selected_symbol}")
        st.json(meta)

        price_data = get_stock_price_data(selected_symbol)

        st.subheader("Summary Statistics")
        st.write(price_data.describe())

        st.subheader("Calculate Daily Returns")
        price_data["Daily Return"] = price_data["Close"].pct_change()
        st.line_chart(price_data["Daily Return"].dropna())

# --- PAGE 5: Stock Price Visualization ---
elif page == "Stock Price Visualization":
    st.title("Stock Price Visualization")

    df_meta = get_all_stocks()
    stock_symbols = df_meta["Symbol"].sort_values().unique()
    selected_symbol = st.selectbox("Select Stock Symbol", stock_symbols)

    if selected_symbol:
        # Fetch and display metadata for the selected symbol
        meta = get_stock_metadata(selected_symbol)
        st.subheader(f"Meta for {selected_symbol}")
        st.json(meta)

        price_data = get_stock_price_data(selected_symbol)

        st.subheader("Price Over Time")
        st.line_chart(price_data["Close"])

        st.subheader("Moving Averages")
        # Calculate simple moving averages (SMA) - 20 and 50 day windows
        price_data["SMA20"] = price_data["Close"].rolling(window=20).mean()
        price_data["SMA50"] = price_data["Close"].rolling(window=50).mean()
        fig_ma = px.line(price_data, y=["Close", "SMA20", "SMA50"],
                         title=f"{selected_symbol} Price and Moving Averages",
                         labels={"value": "Price", "Date": "Date", "variable": "Legend"})
        st.plotly_chart(fig_ma, use_container_width=True)

        st.subheader("Volatility (Rolling Std Dev of Returns)")
        price_data["Daily Return"] = price_data["Close"].pct_change()
        price_data["Volatility"] = price_data["Daily Return"].rolling(window=20).std()
        fig_vol = px.line(price_data, y="Volatility", title=f"{selected_symbol} Volatility (20-day rolling std)")
        st.plotly_chart(fig_vol, use_container_width=True)

