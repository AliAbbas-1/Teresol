import pandas as pd

def df_meta_init(df: pd.DataFrame) -> pd.DataFrame:
    df = df[["Symbol", "Security Name", "ETF", "Listing Exchange", "Market Category"]]
    df = df.dropna(subset=["Listing Exchange", "Market Category"])
    df.set_index(df["Symbol"].str.upper(), inplace=True)
    return df

def df_file_init(df: pd.DataFrame) -> pd.DataFrame:
    df.dropna(inplace=True)
    df["Date"] = pd.to_datetime(df["Date"])
    df.set_index("Date", inplace=True)
    df.sort_index(inplace=True)
    return df

def get_meta_file():
    return df_meta

def get_file():
    return df_file

def get_distribution_data(df: pd.DataFrame):

    result={}

    df['Daily return'] = df["Close"].pct_change()

    result["Avg Daily Return"]=df["Daily return"].mean()
    result["volatility"]=df["Daily return"].std()
    result["Sharp Ratio"]= result["Avg Daily Return"]/result["volatility"]
    result["var 95%"]= result["Avg Daily Return"]-1.65 * result["volatility"]

    print(result)
    return result

def set_df_file(df_new: pd.DataFrame):
    global df_file
    df_file = df_file_init(df_new)


df_meta = df_meta_init(pd.read_csv("app/data/symbols_valid_meta.csv", na_values=[" "]))
df_file = df_file_init(pd.read_csv("app/data/A.csv", na_values=[" "])) # default