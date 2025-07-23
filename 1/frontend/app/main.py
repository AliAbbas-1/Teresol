import streamlit as st
import requests

st.title("Hello!")
res = requests.get("http://localhost:8000/")
res.raise_for_status()

st.json(res.json())