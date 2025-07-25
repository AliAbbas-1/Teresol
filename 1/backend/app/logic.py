from pathlib import Path
import pandas as pd
from typing import Dict
import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.metrics import r2_score, mean_absolute_error, mean_squared_error
from datetime import datetime, timedelta


df_files_list : Dict[str, pd.DataFrame] = {}

def df_meta_init(df: pd.DataFrame) -> pd.DataFrame:
    df = df[["Symbol", "Security Name", "ETF", "Listing Exchange", "Market Category", "Round Lot Size"]].copy()
    df["Market Category"] = df["Market Category"].fillna("Missing")

    df.set_index("Symbol", inplace=True)

    return df
def train_and_predict(df: pd.DataFrame) -> dict:
    df = df.copy()
    df = df[["Date", "Close"]].dropna()
    
    if "Date" not in df.columns:
        df = df.reset_index()

    df['Date_Ordinal'] = df['Date'].map(pd.Timestamp.toordinal)

    x = df[['Date_Ordinal']]
    y = df['Close']

    model = LinearRegression()
    model.fit(x, y)
    y_pred = model.predict(x)

    mae = mean_absolute_error(y, y_pred)
    rmse = np.sqrt(mean_squared_error(y, y_pred))
    r2 = r2_score(y, y_pred)

    last_date = df['Date'].max()
    future_dates = [last_date + timedelta(days=i) for i in range(1, 11)]
    future_date_ordinals = [d.toordinal() for d in future_dates]
    future_preds = model.predict(np.array(future_date_ordinals).reshape(-1, 1))

    future_df = pd.DataFrame({
        'Date': future_dates,
        'Predicted_close': future_preds
    })
    return    {
        "symbol": df.get("Symbol", "Unknown"),
        "metrics": {
            "MAE": mae,
            "RMSE": rmse,
            "R2": r2
        },
        "predictions": future_df.to_dict(orient='records')
    }


def df_file_init(df: pd.DataFrame) -> pd.DataFrame:
    df.dropna(inplace=True)
    df["Date"] = pd.to_datetime(df["Date"])

    df.set_index("Date", inplace=True)

    return df

df_meta = df_meta_init(pd.read_csv("app/.data/symbols_valid_meta.csv", na_values=[" "]))

def get_meta_info():
    global df_meta
    return df_meta

def get_file_data(symbol: str):
    symbol = symbol.upper()
    path = Path(f"app/.data/stocks/{symbol}.csv")

    if not path.exists():
        raise FileNotFoundError(f"File does not exist: {path}")

    df = pd.read_csv(path, na_values=[" "])
    df_files_list[symbol] = df_file_init(df)
    return df_files_list[symbol]

def get_calculation_data(df: pd.DataFrame) -> dict:
    df = df.copy()
    df['Daily return'] = df["Close"].pct_change()

    avg_return = df["Daily return"].mean()
    volatility = df["Daily return"].std()

    if volatility == 0 or pd.isna(volatility):
        sharpe = float('nan')
    else:
        sharpe = avg_return / volatility

    var_95 = avg_return - 1.65 * volatility

    return {
        "Average Daily Return": avg_return,
        "Volatility": volatility,
        "Sharpe Ratio": sharpe,
        "Value At Risk 95%": var_95
    }


def set_df_file(df_new: pd.DataFrame):
    global df_file
    df_file = df_file_init(df_new)