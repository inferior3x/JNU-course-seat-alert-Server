from modules.config.mysql_config import *
import mysql.connector
from mysql.connector import pooling
from mysql.connector import Error


def create_connection():
    try:
        connection = mysql.connector.connect(
            host=HOST,
            database=DATABASE,
            user=USER,
            password=PASSWORD)
        return connection
    except Error as e:
        print(f"Error while connecting to MySQL: {e}")
        return None
    
def create_connection_pool():
    dbconfig = {
    "host": HOST,
    "database": DATABASE,
    "user": USER,
    "password": PASSWORD,
    }

    return pooling.MySQLConnectionPool(pool_name="mypool", pool_size=5, **dbconfig)
