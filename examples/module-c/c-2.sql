-- https://github.com/JSQLParser/JSqlParser/blob/f922e0d8efee673fa1d9176d1d4421b549be09ff/src/test/resources/net/sf/jsqlparser/statement/select/oracle-tests/insert02.sql
insert into shared_table1 values trec;

-- https://github.com/JSQLParser/JSqlParser/blob/f922e0d8efee673fa1d9176d1d4421b549be09ff/src/test/resources/net/sf/jsqlparser/statement/select/oracle-tests/insert03.sql
insert
when (deptno=10) then
  into shared_table2 (empno,ename,job,mgr,sal,deptno)
  values (empno,ename,job,mgr,sal,deptno)
when (deptno=20) then
  into shared_table3 (empno,ename,job,mgr,sal,deptno)
  values (empno,ename,job,mgr,sal,deptno)
when (deptno=30) then
  into table_c1 (empno,ename,job,mgr,sal,deptno)
  values (empno,ename,job,mgr,sal,deptno)
else
  into table_c2 (empno,ename,job,mgr,sal,deptno)
  values (empno,ename,job,mgr,sal,deptno)
select * from table_c3;
