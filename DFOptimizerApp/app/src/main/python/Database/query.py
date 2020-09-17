import pandas as pd
from mysql.connector import connection
from sqlalchemy import create_engine


class SqlQuery:

    def __init__(self, database="sports_db"):
        self.conn = connection.MySQLConnection(
            host="dfi-db.csguqrzjnw8k.us-east-2.rds.amazonaws.com",
            user="guest",
            password="password",
            database=database,
        )
        self.cursor = self.conn.cursor()

    def __enter__(self):
        return self

    # displays all tables currently in database
    #
    def show_all_tables(self):
        self.cursor.execute("SELECT name FROM sqlite_master WHERE type='table';")
        tables = self.cursor.fetchall()
        print(tables)
    #
    # get specific table in database
    # table is name of table   # string
    # args are arguments  #string     # example    "WHERE x > 5"
    #
    def get_table(self, table, select = '*', args = '', dataframe=False):
        self.cursor.execute(f"SELECT {select} FROM {table} {args};")
        fetched_table = self.cursor.fetchall()
        if dataframe:
            field_names = [i[0] for i in self.cursor.description]
            return pd.DataFrame(fetched_table, columns=field_names)

        return fetched_table
    # #
    # # update existing row
    # #
    def update_row(self, data, table, columns, args):
        self.cursor.execute(f"UPDATE {table} SET {columns}= {data} {args};")
        self.conn.commit()
    
    def update_many(self, data, table, columns="", args=""):
        self.cursor.executemany(f"UPDATE {table} SET {columns} WHERE {args};", data)
        self.conn.commit()

    #
    # insert single value into table
    #
    def insert(self, data, table, columns="", args=""):

        # if dataframe object is passed in
        if isinstance(data, pd.DataFrame):
            self.data_frame_insert(data, table)   # calls static dataframe method
            return
        # if tuple is passed in
        q_marks = ', '.join(['%s'] * len(data))
        self.cursor.execute(f"INSERT INTO {table} {columns} VALUES ({q_marks}) {args};", data)
        self.conn.commit()

    #
    # insert many rows into table accepted data types --- pandas Dataframe or list of tuples
    # data is data to insert   # can be passed as dataframe or list of tuples
    # dataframe is true if dataframe is passed in
    def insert_many(self, data, table="", columns="", dataframe = True):

        # if user passes in dataframe object
        if isinstance(data, pd.DataFrame):
            self.data_frame_insert(data, table)   # calls static dataframe method
            return
        # if tuple is passed in
        q_mark = ', '.join(['%s'] * len(data[0]))

        self.cursor.executemany(f"INSERT INTO {table} {columns} VALUES ({q_mark});", data)
        self.conn.commit()

    #
    # insert data frame into mysql
    # data is dataframe
    # table is name of table to insert data    # string
    #
    @staticmethod
    def data_frame_insert(data, table):
        engine = create_engine('mysql+mysqlconnector://root:dfi-db.csguqrzjnw8k.us-east-2.rds.amazonaws.com', echo=False)
        data.to_sql(name=table, con=engine, if_exists = 'append', index=False)

    #
    # calls my sql procedure
    # procedure is the name of sql procedure     # string
    # args to pass as parameters to procedure    # tuple object
    # dataframe true if want dataframe as return # bool
    #
    def call_procedure(self, procedure, args=(), dataframe=False):
        self.cursor.callproc(procedure, args=args)
        results = [r.fetchall() for r in self.cursor.stored_results()]

        if dataframe:
            return pd.DataFrame(results[0])
        return results[0]

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.conn.commit()
        self.conn.close()