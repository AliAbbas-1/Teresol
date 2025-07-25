import streamlit as st
import pandas as pd
import requests
import plotly.express as px
import seaborn as sns
import matplotlib.pyplot as plt

st.set_page_config(layout="wide", page_title="NASDAQ Dashboard")

API_BASE = "http://localhost:8000"

# ---------- SIDEBAR ----------
st.sidebar.title("NASDAQ Explorer")
section = st.sidebar.radio("Select View", ["Market Overview", "Stock Explorer", "Advanced Insights"])


# ---------- PAGE 1: MARKET OVERVIEW ----------
if section == "Market Overview":
    st.title("NASDAQ Market Overview")

    # ROW 1: Market Category (Pie) and ETF Distribution (Pie) side-by-side
    col1_row1, col2_row1 = st.columns(2) # Two columns for these two charts

    # Market category (Pie Chart) - in col1_row1
    categories = requests.get(f"{API_BASE}/distribution/categories").json()
    fig_cat = px.pie(names=list(categories.keys()), values=list(categories.values()),
                     title="Market Category Distribution")
    col1_row1.plotly_chart(fig_cat, use_container_width=True)

    # ETF distribution (Pie Chart - kept as pie chart) - in col2_row1
    etf_dist = requests.get(f"{API_BASE}/distribution/etf").json()
    fig_etf_pie = px.pie(names=["Non-ETFs", "ETFs"], values = [etf_dist.get("N", 0), etf_dist.get("Y", 0)],
                     title="ETF vs Non-ETF")
    col2_row1.plotly_chart(fig_etf_pie, use_container_width=True) # Use the original pie chart figure


    # ROW 2: Stocks per Exchange (Bar) - on a new line below
    st.markdown("---") # Optional: Add a separator
    st.subheader("Stocks per Exchange Distribution") # Optional: New subheader for clarity

    # Distribution by exchange (Bar Graph) - this will automatically go to a new line
    exchanges = requests.get(f"{API_BASE}/distribution/exchanges").json()
    fig_exchange = px.bar(x=list(exchanges.keys()), y=list(exchanges.values()),
                          labels={"x": "Exchange", "y": "Count"}, title="Stocks per Exchange")
    st.plotly_chart(fig_exchange, use_container_width=True) # Directly use st.plotly_chart


# ---------- PAGE 2: STOCK EXPLORER ----------
elif section == "Stock Explorer":
    st.title("Explore Individual Stocks")

    stock_data = requests.get(f"{API_BASE}/stocks").json()
    symbol_df = pd.DataFrame(stock_data)

    selected_symbol = st.selectbox("Select Stock Symbol", symbol_df["Symbol"].sort_values())

    if selected_symbol:
        # Metadata
        st.subheader(f"Metadata for {selected_symbol}")
        meta = requests.get(f"{API_BASE}/stocks/{selected_symbol}").json()
        st.json(meta)

        # Price over time
        st.subheader("Price Over Time")
        price_data = pd.DataFrame(requests.get(f"{API_BASE}/stocks/{selected_symbol}/data").json())
        price_data["Date"] = pd.to_datetime(price_data["Date"])
        price_data.set_index("Date", inplace=True)

        st.line_chart(price_data["Close"])

        # Analytics
        st.subheader("Analytics")
        analytics = requests.get(f"{API_BASE}/stocks/{selected_symbol}/analytics").json()
        st.json(analytics)



# ---------- PAGE 3: ADVANCED INSIGHTS ----------
elif section == "Advanced Insights":
    st.title("Advanced Insights")

    df_meta = pd.DataFrame(requests.get(f"{API_BASE}/stocks").json())

    st.subheader("Top 10 Companies by Round Lot Size")
    if "Round Lot Size" in df_meta.columns:
        df_meta["Round Lot Size"] = pd.to_numeric(df_meta["Round Lot Size"], errors="coerce")
        top10 = df_meta.sort_values("Round Lot Size", ascending=False).head(10)
        st.table(top10[["Symbol", "Security Name", "Round Lot Size"]])

        st.subheader("Scatter Plot: Round Lot Size vs Symbol")
        fig_scatter = px.scatter(top10, x="Symbol", y="Round Lot Size", size="Round Lot Size", color="Symbol")
        st.plotly_chart(fig_scatter, use_container_width=True)
    else:
        st.warning("Round Lot Size column not found.")

    st.subheader("Correlation Matrix (Numerical Columns)")
    num_df = df_meta.select_dtypes(include=["float64", "int64"]).dropna(axis=1, how="any")
    if not num_df.empty:
        corr = num_df.corr()
        fig_corr, ax = plt.subplots(figsize=(10, 6))
        sns.heatmap(corr, annot=True, cmap="coolwarm", ax=ax)
        st.pyplot(fig_corr)
    else:
        st.info("No numeric columns found for correlation matrix.")