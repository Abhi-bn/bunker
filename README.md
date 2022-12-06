This file consists of commnads that are supported by our database and syntax of each command: - 

SUPPORTED DATA TYPES : -
	->TINY INT
	->SMALL INT
	->INT
	->BIG INT
	->FLOAT
	->DOUBLE
	->YEAR
	->TIME
	->DATE TIME
	->DATE
	->TEXT

DATA DEFINITION LANGUAGE COMMANDS: -

	->SHOW DATABASES;

	->CREATE DATABASE <database_name>;

	->USE DATABASE <database_name>;

	->SHOW TABLES;

	->CREATE TABLE <table_name> <column_name> <data_type>,......;

	SUPPORTED CONSTRAINTS FOR CREATE TABLE: - (unique, nullable)
		->CREATE TABLE <table_name> <column_name> <data_type> unique;

		->CREATE TABLE <table_name> <column_name> <data_type> nullable;

	->DROP TABLE <table_name>;

	->DROP DATABASE <database_name>;

	->EXIT;


DATA MANIPULATION LANGUAGE COMMANDS: -

	->INSERT INTO <table_name> <value1>, ......;

	UPDATE COMMAND USAGE
		->UPDATE <table_name> set <column_name> = <value> where <column_name> <OPERATOR> <value>;

		->UPDATE <table_name> set <column_name> = <value> where <column1> <OPERATOR> <value1>, <column2> <OPERATOR> <value2>;

			MULTI FIELD UPDATION IS ALSO SUPPORTED SYNTAX IS AS FOLLOWS: -

			->UPDATE <table_name> set <column_name1> = <value1>, <column_name2> <OPERATOR> <value2> where <column1> <OPERATOR> <value1>;

	DELETE COMMAND USAGE

		->DELETE <table_name> where <column_name> <OPERATOR> <value1>;

		MULTI FIELD DELETION IS ALSO SUPPORTED SYNTAX IS AS FOLLOWS: -

			->DELETE <table_name> where <column_name1> <OPERATOR> <value1>, <column_name2> <OPERATOR> <value2>;


DATA QUERY LANGUAGE: -

	->SELECT * FROM <table_name>;

	SELECT WITH ONLY SOME FIELDS OF THE TABLE : -

		->SELECT <column_name> FROM <table_name>;

	SELECT WITH WHERE CLAUSE : -
	
		->SELECT * FROM <table_name> where <column1> <OPERATOR> <value1>;

	SELECT WITH WHERE CLAUSE AND COLUMN SELECTION: -
	
		->SELECT <column_name> FROM <table_name> where <column1> <OPERATOR> <value1>;

	SELECT WITH WHERE MULTIPLE WHERE CLAUSES: - 	

		->SELECT * FROM <table_name> where <column1> <OPERATOR> <value1>, <column2> <OPERATOR> <value2>;

	SELECT WITH MULTIPLE WHERE CLAUSE AND COLUMN SELECTION: -
	
		->SELECT <column_name1>, <column_name2> FROM <table_name> where <column1> <OPERATOR> <value1>, <column2> <OPERATOR> <value2>;


OPERATOR'S SUPPORTED(WHERE EVER<OPERATOR> is used we can make use of the below operators for appropriate data types) : -
	=	>	<	!=	<=	>=	

EXAMPLE COMMANDS FOR TESTING : -

CREATE DATABASE TEST;

USE TEST;

SHOW DATABASES;

CREATE TABLE TESTINGTABLE column1 int, column2 int, column3 text;

DESCRIBE TABLE TESTINGTABLE;

DESCRIBE;

SHOW TABLES;

INSERT COMMANDS: - 
	INSERT INTO TESTINGTABLE 1, 1, column2Value1;
	INSERT INTO TESTINGTABLE 2, 2, column2Value2; 
	INSERT INTO TESTINGTABLE 3, 2, column2Value3; 
	INSERT INTO TESTINGTABLE 4, 4, column2Value4;

SELECT * FROM testingTable1;

SELECT column1 FROM TESTINGTABLE;

SELECT * FROM TESTINGTABLE WHERE COLUMN1 = 4,COLUMN2 = 4;

SELECT column1 FROM TESTINGTABLE WHERE COLUMN1 = 4,COLUMN2 = 4;

SELECT column1 FROM TESTINGTABLE WHERE COLUMN1 != 4;

UPDATE TESTINGTABLE SET COLUMN1 = 100 WHERE COLUMN1 = 1;

SELECT * FROM TESTINGTABLE;

DELETE TABLE TESTINGTABLE WHERE COLUMN1 = 100;

SELECT * FROM TESTINGTABLE;

DROP DATABASE TEST;