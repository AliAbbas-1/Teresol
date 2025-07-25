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
    st.title("📊 NASDAQ Market Overview")

    col1, col2, col3 = st.columns(3)

    # Unique stocks
    unique_resp = requests.get(f"{API_BASE}/stocks/unique").json()
    col1.metric("Unique Stocks", unique_resp["unique_records"])

    # Distribution by exchange
    exchanges = requests.get(f"{API_BASE}/distribution/exchanges").json()
    fig_exchange = px.bar(x=list(exchanges.keys()), y=list(exchanges.values()), 
                          labels={"x": "Exchange", "y": "Count"}, title="Stocks per Exchange")
    col2.plotly_chart(fig_exchange, use_container_width=True)

    # Market category
    categories = requests.get(f"{API_BASE}/distribution/categories").json()
    fig_cat = px.pie(names=list(categories.keys()), values=list(categories.values()), 
                     title="Market Category Distribution")
    col3.plotly_chart(fig_cat, use_container_width=True)

    # ETF distribution
    etf_dist = requests.get(f"{API_BASE}/distribution/etf").json()
    fig_etf = px.pie(names=["Non-ETFs", "ETFs"], values=[etf_dist.get(False, 0), etf_dist.get(True, 0)],
                     title="ETF vs Non-ETF")
    st.plotly_chart(fig_etf, use_container_width=True)



# ---------- PAGE 2: STOCK EXPLORER ----------
elif section == "Stock Explorer":
    st.title("🔍 Explore Individual Stocks")

    stock_data = requests.get(f"{API_BASE}/stocks").json()
    symbol_df = pd.DataFrame(stock_data)

    selected_symbol = st.selectbox("Select Stock Symbol", symbol_df["Symbol"].sort_values())

    if selected_symbol:
        st.subheader(f"Metadata for {selected_symbol}")
        meta = requests.get(f"{API_BASE}/stocks/{selected_symbol}").json()
        st.json(meta)

        st.subheader("📈 Price Over Time")
        price_data = pd.DataFrame(requests.get(f"{API_BASE}/stocks/{selected_symbol}/data").json())
        price_data["Date"] = pd.to_datetime(price_data["Date"])
        price_data.set_index("Date", inplace=True)

        st.line_chart(price_data["Close"])

        st.subheader("📉 Analytics")
        analytics = requests.get(f"{API_BASE}/stocks/{selected_symbol}/analytics").json()
        analytics_df = pd.DataFrame(analytics)

        col1, col2 = st.columns(2)
        col1.line_chart(analytics_df["Moving Average"])
        col2.line_chart(analytics_df["Volatility"])



# ---------- PAGE 3: ADVANCED INSIGHTS ----------
elif section == "Advanced Insights":
    st.title("🧠 Advanced Insights")

    df_meta = pd.DataFrame(requests.get(f"{API_BASE}/stocks").json())

    st.subheader("Top 10 Companies by Round Lot Size")
    if "Round Lot Size" in df_meta.columns:
        df_meta["Round Lot Size"] = pd.to_numeric(df_meta["Round Lot Size"], errors="coerce")
        top10 = df_meta.sort_values("Round Lot Size", ascending=False).head(10)
        st.table(top10[["Symbol", "Security Name", "Round Lot Size"]])

        st.subheader("📍 Scatter Plot: Round Lot Size vs Symbol")
        fig_scatter = px.scatter(top10, x="Symbol", y="Round Lot Size", size="Round Lot Size", color="Symbol")
        st.plotly_chart(fig_scatter, use_container_width=True)
    else:
        st.warning("Round Lot Size column not found.")

    st.subheader("📊 Correlation Matrix (Numerical Columns)")
    num_df = df_meta.select_dtypes(include=["float64", "int64"]).dropna(axis=1, how="any")
    if not num_df.empty:
        corr = num_df.corr()
        fig_corr, ax = plt.subplots(figsize=(10, 6))
        sns.heatmap(corr, annot=True, cmap="coolwarm", ax=ax)
        st.pyplot(fig_corr)
    else:
        st.info("No numeric columns found for correlation matrix.")
