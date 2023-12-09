import requests

def send_push_notification(expo_push_token, title, body, data):
    message = {
        "to": expo_push_token,
        "sound": "default",
        "title": title,
        "body": body,
        "data": data,
    }

    requests.post(
        'https://exp.host/--/api/v2/push/send',
        json=message,
        headers={
            "Accept": "application/json",
            "Accept-encoding": "gzip, deflate",
            "Content-Type": "application/json",
        }
    )