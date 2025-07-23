import pandas as pd
import numpy as np

df = pd.read_csv("app/data/symbols_valid_meta.csv")
df = df[["Symbol", "Security Name", "ETF"]]
df.dropna(subset=["Symbol", "Security Name"], inplace=True)

def get_all_data():
    return df

# TODO
# load files
# extract columns
# do required calculations
# access functions in app.routes