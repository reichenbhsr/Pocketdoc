set PGPASSWORD=password
@echo off
"C:\Program Files\PostgreSQL\9.3\bin\psql.exe" -h localhost -U postgres -d pocketdoc -f CLEAN_TABLES.txt
"C:\Program Files\PostgreSQL\9.3\bin\psql.exe" -h localhost -U postgres -d pocketdoc -f CREATE_TABLES.txt
"C:\Program Files\PostgreSQL\9.3\bin\psql.exe" -h localhost -U postgres -d pocketdoc -f INSERT_TESTDATA.txt
pause