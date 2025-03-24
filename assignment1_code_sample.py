import os
import smtplib
from email.mime.text import MIMEText
from urllib.parse import urlparse

import pymysql  # type: ignore
import requests

db_config = {
    'host': os.getenv('Host'),
    'user': os.getenv('User'),
    'password': os.getenv('Password')
}

def get_user_input():
    user_input = input('Enter your name: ')
    return user_input

def send_email(to, subject, body):
    msg = MIMEText(body)
    msg['Subject'] = subject
    msg['From'] = 'noreply@example.com'
    msg['To'] = to

    with smtplib.SMTP('localhost') as server:
        server.send_message(msg)

def get_data(url):
    # Parse the URL
    parsed_url = urlparse(url)

    # Validate the URL scheme
    if parsed_url.scheme not in ['http', 'https']:
        raise ValueError("Invalid URL scheme. Only HTTP and HTTPS are allowed.")

    # Validate the network location (hostname)
    if not parsed_url.netloc:
        raise ValueError("Invalid URL. Missing network location (hostname).")

    # Use requests to fetch the data
    try:
        response = requests.get(url, timeout=5, verify=True)
        response.raise_for_status()  # Raise an HTTPError for bad responses (4xx and 5xx)
        return response.text
    except requests.exceptions.RequestException as e:
        raise RuntimeError(f"Failed to fetch data from the URL: {e}")

def save_to_db(data):
    query = "INSERT INTO mytable (column1, column2) VALUES (%s, %s)"
    connection = pymysql.connect(**db_config)
    cursor = connection.cursor()
    cursor.execute(query, (data, 'Another Value'))
    connection.commit()
    cursor.close()
    connection.close()

if __name__ == '__main__':
    user_input = get_user_input()
    url = "http://example.com/data"  # Replace with the actual URL
    data = get_data(url)
    save_to_db(data)
    send_email('admin@example.com', 'User Input', user_input)