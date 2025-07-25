import streamlit as st
import requests
import pandas as pd
import matplotlib.pyplot as plt

API_URL = "http://127.0.0.1:8000"

st.set_page_config(page_title="Stock Info Dashboard", layout="wide")

st.title("📊 Stock Information Dashboard")

# Tabs for navigation
tabs = st.tabs(["All Stocks", "Symbol Lookup", "Distributions"])

# --- All Stocks Tab ---
with tabs[0]:
    st.subheader("All Stock Symbols")
    response = requests.get(f"{API_URL}/stocks")
    if response.status_code == 200:
        data = response.json()
        df = pd.DataFrame(data)
        st.dataframe(df)

        st.metric("Total Symbols", len(df))

    else:
        st.error("Failed to fetch stock data.")

# --- Symbol Lookup Tab ---
with tabs[1]:
    st.subheader("Lookup Symbol Details")

    symbol_input = st.text_input("Enter Symbol (e.g., AAPL, TSLA)").upper()

    if symbol_input:
        res = requests.get(f"{API_URL}/stocks/{symbol_input}")
        if res.status_code == 200:
            symbol_data = res.json()
            st.json(symbol_data["meta"], expanded=False)
            st.subheader("Calculation Data")
            st.json(symbol_data["calc"], expanded=False)
        else:
            st.error(res.json().get("error", "Symbol not found."))

# --- Distributions Tab ---
with tabs[2]:
    st.subheader("Stock Distributions")

    col1, col2, col3 = st.columns(3)

    def plot_distribution(endpoint, title, col):
        res = requests.get(f"{API_URL}{endpoint}")
        if res.status_code == 200:
            dist_data = res.json()
            dist_series = pd.Series(dist_data)
            fig, ax = plt.subplots()
            dist_series.plot(kind="bar", ax=ax)
            ax.set_title(title)
            ax.set_ylabel("Count")
            ax.tick_params(axis='x', rotation=45)
            col.pyplot(fig)
        else:
            col.error(f"Failed to load {title.lower()} data.")

    plot_distribution("/distribution/etf", "ETF Distribution", col1)
    plot_distribution("/distribution/exchanges", "Exchange Distribution", col2)
    plot_distribution("/distribution/categories", "Category Distribution", col3)
###done
