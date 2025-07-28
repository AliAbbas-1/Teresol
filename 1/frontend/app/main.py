import streamlit as st
import pandas as pd
import requests
import plotly.express as px
import matplotlib.pyplot as plt
import seaborn as sns

st.set_page_config(layout="wide", page_title="NASDAQ Dashboard")
API_BASE = "http://localhost:8000"

# --- Cached API Calls ---
@st.cache_data
def get_all_stocks():
    return pd.DataFrame(requests.get(f"{API_BASE}/stocks").json())

@st.cache_data
def get_etf_dist():
    return requests.get(f"{API_BASE}/distribution/etf").json()

@st.cache_data
def get_categories_dist():
    return requests.get(f"{API_BASE}/distribution/categories").json()

@st.cache_data
def get_stock_metadata(symbol):
    return requests.get(f"{API_BASE}/stocks/{symbol}").json()

@st.cache_data
def get_stock_price_data(symbol):
    df = pd.DataFrame(requests.get(f"{API_BASE}/stocks/{symbol}/data").json())
    df["Date"] = pd.to_datetime(df["Date"])
    df.set_index("Date", inplace=True)
    return df.sort_index()

# --- Sidebar Navigation ---
st.sidebar.title(" NASDAQ Dashboard")

main_section = st.sidebar.radio("Select Section", [
    "Meta Basic Analysis",
    "Meta Visualization",
    "Meta Adv. Analysis",
    "Stock Price Data",
    "Stock Visualization"
])

# --- Sub-section selections ---
data_page = None
viz_page = None

if main_section == "Stock Price Data":
    with st.sidebar.expander(" Stock Price Data Topics", expanded=True):
        data_page = st.radio("Choose Data", ["Summary Stats", "Daily Returns"], key="data_radio")

if main_section == "Stock Visualization":
    with st.sidebar.expander(" Visualization Topics", expanded=True):
        viz_page = st.radio("Choose Visualization", ["Price Over Time", "Moving Averages", "Volatility"], key="viz_radio")

# --- Meta Basic Analysis ---
if main_section == "Meta Basic Analysis":
    st.title("Meta Basic Analysis")
    df = get_all_stocks()
    st.metric("Unique Stocks", df["Symbol"].nunique())
    st.subheader("Exchanges Distribution")
    st.dataframe(df["Listing Exchange"].value_counts())
    st.metric("ETFs", get_etf_dist().get("Y", 0))

# --- Meta Visualization ---
elif main_section == "Meta Visualization":
    st.title("Meta Visualization")
    df = get_all_stocks()
    st.subheader("Stocks per Listing Exchange")
    st.plotly_chart(
        px.bar(
            x=df["Listing Exchange"].value_counts().index,
            y=df["Listing Exchange"].value_counts().values
        ),
        use_container_width=True
    )
    st.subheader("Market Category Distribution")
    cat = get_categories_dist()
    st.plotly_chart(
        px.pie(names=cat.keys(), values=cat.values(), title="Market Categories"),
        use_container_width=True
    )

# --- Meta Advanced Analysis ---
elif main_section == "Meta Adv. Analysis":
    st.title("Meta Advanced Analysis")
    df_meta = get_all_stocks()
    etf = get_etf_dist()
    st.subheader("ETF vs Non-ETF")
    st.plotly_chart(
        px.pie(
            names=["ETFs", "Non-ETFs"],
            values=[etf.get("Y", 0), etf.get("N", 0)]
        ),
        use_container_width=True
    )

    # Companies by lot size
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

# --- Stock Price Data Subpages ---
elif main_section == "Stock Price Data" and data_page:
    st.title(f"Stock Data - {data_page}")
    df = get_all_stocks()
    symbol = st.selectbox("Select Stock", df["Symbol"].sort_values(), key="price_select")
    data = get_stock_price_data(symbol)

    #  Always show metadata at the top
    st.subheader(" Meta Info")
    st.json(get_stock_metadata(symbol))

    # Conditional Subpage Content
    if data_page == "Summary Stats":
        st.subheader(" Summary Statistics")
        st.write(data.describe())

    elif data_page == "Daily Returns":
        st.subheader(" Daily Returns")
        data["Daily Return"] = data["Close"].pct_change()
        st.line_chart(data["Daily Return"].dropna())

# --- Stock Visualization Subpages ---
elif main_section == "Stock Visualization" and viz_page:
    st.title(f"Visualization - {viz_page}")
    df = get_all_stocks()
    symbol = st.selectbox("Select Stock for Visualization", df["Symbol"].sort_values(), key="viz_select")
    data = get_stock_price_data(symbol)

    # Always show metadata at the top
    st.subheader(" Meta Info")
    st.json(get_stock_metadata(symbol))

    # Conditional Visualization Content
    if viz_page == "Price Over Time":
        st.subheader(" Price Over Time")
        st.line_chart(data["Close"])

    elif viz_page == "Moving Averages":
        st.subheader(" Moving Averages")
        data["SMA20"] = data["Close"].rolling(20).mean()
        data["SMA50"] = data["Close"].rolling(50).mean()
        st.plotly_chart(px.line(data, y=["Close", "SMA20", "SMA50"]), use_container_width=True)

    elif viz_page == "Volatility":
        st.subheader(" Volatility (Rolling Std Dev)")
        data["Volatility"] = data["Close"].pct_change().rolling(20).std()
        st.plotly_chart(px.line(data, y="Volatility"), use_container_width=True)
