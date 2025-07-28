import streamlit as st
import pandas as pd
import requests
import plotly.express as px
import matplotlib.pyplot as plt

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
    df = get_all_stocks()
    etf = get_etf_dist()
    st.subheader("ETF vs Non-ETF")
    st.plotly_chart(
        px.pie(
            names=["ETFs", "Non-ETFs"],
            values=[etf.get("Y", 0), etf.get("N", 0)]
        ),
        use_container_width=True
    )

# --- Stock Price Data Subpages ---
elif main_section == "Stock Price Data" and data_page:
    st.title(f"Stock Data - {data_page}")
    df = get_all_stocks()
    symbol = st.selectbox("Select Stock", df["Symbol"].sort_values(), key="price_select")
    data = get_stock_price_data(symbol)

    #  Always show metadata at the top
    st.subheader(" Meta Info")
    st.json(get_stock_metadata(symbol))

    # --- Conditional Subpage Content ---
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

    # --- Conditional Visualization Content ---
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
