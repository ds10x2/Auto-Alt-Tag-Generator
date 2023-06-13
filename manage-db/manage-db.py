import pymysql
import os
from dotenv import load_dotenv

load_dotenv()

# RDS 연결 정보
host = os.getenv('host')
port = 3306  
username = os.getenv('username')
password = os.getenv('password')
database = 'images_alt'


conn = pymysql.connect(host=host, port=port, user=username, password=password, database=database)

try:

    cursor = conn.cursor()
    delete_query = "DELETE FROM images WHERE id > 0"
    cursor.execute(delete_query)
    conn.commit()
    
    alter_query = "ALTER TABLE images AUTO_INCREMENT = 1"
    cursor.execute(alter_query)
    conn.commit()

    print("행이 성공적으로 삭제되었습니다.")
    print("AUTO_INCREMENT가 1로 재설정되었습니다.")

except Exception as e:
    print("삭제 중 오류가 발생했습니다:", str(e))
    conn.rollback()

finally:
    conn.close()

