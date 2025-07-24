import streamlit as st
import requests

if 'theme' not in st.session_state:
    st.session_state.theme = 'light'

def toggle_theme():
    if st.session_state.theme == 'light':
        st.session_state.theme = 'dark'
        st._config.set_option('theme.base', 'dark')
    else:
        st.session_state.theme = 'light'
        st._config.set_option('theme.base', 'light')
    st.rerun() 

st.sidebar.button(
    "Toggle Theme: " + ("☀️ Light" if st.session_state.theme == 'light' else "🌙 Dark"),
    on_click=toggle_theme
)

st.title("Hello")

# res = requests.get("http://localhost:8000/")
# res.raise_for_status()
# st.json(res.json())

upload_csv = st.file_uploader("Upload your CSV file", type=["csv"])
if upload_csv is not None:
    upload_csv.seek(0)
    files = {
        'file': (upload_csv.name, upload_csv, 'text/csv')
    }
    response = requests.post("http://localhost:8000/upload", files=files)
    if response.status_code == 200:
        st.success("Success")
        st.json(response.json())
    else:
        st.error("Failure")