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
    "Stock Visualization",
    "Upload File"
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
    fig = px.bar(
        x=df["Listing Exchange"].value_counts().index,
        y=df["Listing Exchange"].value_counts().values
    )
    fig.update_layout(
        xaxis_title="Listing Exchange",
        yaxis_title="Stocks"
    )
    st.plotly_chart(fig, use_container_width=True)

    st.subheader("Market Category Distribution")
    cat = get_categories_dist()
    
    descriptive_categories = {}
    for key, value in cat.items():
        if key == 'Q':
            descriptive_categories['Global Select Market'] = value
        elif key == 'G':
            descriptive_categories['Global Market'] = value
        elif key == 'S':
            descriptive_categories['Capital Market'] = value
        elif key == 'Missing':
            descriptive_categories['Missing Data'] = value # or just 'Missing'
        else:
            descriptive_categories[key] = value # Handle any unexpected keys gracefully

    st.plotly_chart(
        px.pie(names=list(descriptive_categories.keys()),
            values=list(descriptive_categories.values()),
            title="NASDAQ Market Categories"),
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

        # Scatter plot
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
        if corr.shape[0] == 1 and corr.shape[1] == 1:
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

    # Show metadata as table
    meta = get_stock_metadata(symbol)
    st.subheader("Meta Info")
    st.table(pd.DataFrame([meta]))

    # Conditional Subpage Content
    if data_page == "Summary Stats":
        st.subheader(" Summary Statistics")
        st.write(data.describe())

    elif data_page == "Daily Returns":
        st.subheader("Daily Returns")
        data["Daily Return"] = data["Close"].pct_change()
        df = data["Daily Return"].dropna().reset_index()
        df.columns = ["Date", "Daily Return"]
        fig = px.line(df, x="Date", y="Daily Return", 
                    labels={"Date": "", "Daily Return": "Daily Return"})
        st.plotly_chart(fig, use_container_width=True)

# --- Stock Visualization Subpages ---
elif main_section == "Stock Visualization" and viz_page:
    st.title(f"Visualization - {viz_page}")
    df = get_all_stocks()
    symbol = st.selectbox("Select Stock for Visualization", df["Symbol"].sort_values(), key="viz_select")
    data = get_stock_price_data(symbol)

    # Show metadata as table
    meta = get_stock_metadata(symbol)
    st.subheader("Meta Info")
    st.table(pd.DataFrame([meta]))

    # Conditional Visualization Content
    if viz_page == "Price Over Time":
        st.subheader(" Price Over Time")
        df = data["Close"].dropna().reset_index()
        df.columns = ["Date", "Close"]
        fig = px.line(df, x="Date", y="Close",
                    labels={"Date": "", "Close": "Close"})
        st.plotly_chart(fig, use_container_width=True)

    elif viz_page == "Moving Averages":
        st.subheader(" Moving Averages")
        data["SMA20"] = data["Close"].rolling(20).mean()
        data["SMA50"] = data["Close"].rolling(50).mean()
        fig = px.line(data, y=["Close", "SMA20", "SMA50"])
        fig.update_layout(
            xaxis_title="",
            yaxis_title="Averages"
        )
        st.plotly_chart(fig, use_container_width=True)

    elif viz_page == "Volatility":
        st.subheader(" Volatility (Rolling Std Dev)")
        data["Volatility"] = data["Close"].pct_change().rolling(20).std()
        fig = px.line(data, y="Volatility")
        fig.update_layout(xaxis_title="")
        st.plotly_chart(fig, use_container_width=True)

elif main_section == "Upload File":
    st.title("File Upload")
    uploaded_file = st.file_uploader("Choose a file", type=["csv", "txt"])
    if uploaded_file is not None:
        st.write("Filename:", uploaded_file.name)

        if st.button("Upload"):
            files = {"file": (uploaded_file.name, uploaded_file, uploaded_file.type)}
            try:
                response = requests.post("http://localhost:8000/upload", files=files)
                json_data = response.json()

                meta = json_data["metadata"]
                st.subheader("Meta Info")
                st.table(pd.DataFrame([meta]))

                # Initial data processing
                # Create a copy to avoid modifying the original data dictionary if it were used elsewhere
                processed_data = pd.DataFrame(json_data["data"])
                processed_data["Date"] = pd.to_datetime(processed_data["Date"])
                processed_data.set_index("Date", inplace=True)
                processed_data = processed_data.sort_index()

                st.subheader("Summary Statistics")
                st.write(processed_data.describe())

                st.subheader("Daily Returns")
                # Calculate daily returns on a copy of the 'Close' column to avoid modifying 'processed_data'
                daily_returns_df = processed_data["Close"].pct_change().dropna().reset_index()
                daily_returns_df.columns = ["Date", "Daily Return"]
                fig_daily_returns = px.line(daily_returns_df, x="Date", y="Daily Return",
                                            labels={"Date": "", "Daily Return": "Daily Return"})
                st.plotly_chart(fig_daily_returns, use_container_width=True)

                st.subheader("Price Over Time")
                # Extract 'Close' prices for plotting without modifying 'processed_data'
                price_over_time_df = processed_data["Close"].dropna().reset_index()
                price_over_time_df.columns = ["Date", "Close"]
                fig_price_over_time = px.line(price_over_time_df, x="Date", y="Close",
                                            labels={"Date": "", "Close": "Close"})
                st.plotly_chart(fig_price_over_time, use_container_width=True)

                st.subheader("Moving Averages")
                # Calculate moving averages directly on 'processed_data' as new columns
                # This is acceptable as these are new derived columns for a specific plot
                processed_data["SMA20"] = processed_data["Close"].rolling(20).mean()
                processed_data["SMA50"] = processed_data["Close"].rolling(50).mean()
                fig_moving_averages = px.line(processed_data, y=["Close", "SMA20", "SMA50"])
                fig_moving_averages.update_layout(
                    xaxis_title="",
                    yaxis_title="Averages"
                )
                st.plotly_chart(fig_moving_averages, use_container_width=True)

                st.subheader("Volatility (Rolling Std Dev)")
                # Calculate volatility directly on 'processed_data' as a new column
                processed_data["Volatility"] = processed_data["Close"].pct_change().rolling(20).std()
                fig_volatility = px.line(processed_data, y="Volatility")
                fig_volatility.update_layout(xaxis_title="")
                st.plotly_chart(fig_volatility, use_container_width=True)


                # here goes: price over time, moving avg, volatility
                
            except Exception as e:
                st.error(f"Failure: {response.text}")