-- https://github.com/JSQLParser/JSqlParser/blob/f922e0d8efee673fa1d9176d1d4421b549be09ff/src/test/resources/net/sf/jsqlparser/statement/select/oracle-tests/insert05.sql
insert all into shared_table1 (pid, fname, lname)
values (1, 'dan', 'morgan')
into shared_table2 (pid, fname, lname)
values (2, 'jeremiah', 'wilton')
into shared_table3 (pid, fname, lname)
values (3, 'helen', 'lofstrom')
select *
from table_a1
