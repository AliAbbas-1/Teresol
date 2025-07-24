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

def get_all_metadata():
    return df_meta

# def get_distribution_data(df: pd.DataFrame) [return dictionary] :

#     object["Daily Value Return"] = [...]
#     object[]
#     object[]
#     object[]

#     return object

def set_df_file(df_new: pd.DataFrame):
    global df_file
    df_file = df_file_init(df_new)


df_meta = df_meta_init(pd.read_csv("app/data/symbols_valid_meta.csv", na_values=[" "]))
df_file = df_file_init(pd.read_csv("app/data/A.csv", na_values=[" "])) # default