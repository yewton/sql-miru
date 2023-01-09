-- https://github.com/JSQLParser/JSqlParser/blob/f922e0d8efee673fa1d9176d1d4421b549be09ff/src/test/resources/net/sf/jsqlparser/statement/select/oracle-tests/for_update01.sql
select employee_id from (select * from shared_table1)
    for update of employee_id;

-- https://github.com/JSQLParser/JSqlParser/blob/f922e0d8efee673fa1d9176d1d4421b549be09ff/src/test/resources/net/sf/jsqlparser/statement/select/oracle-tests/join01.sql
select d.department_id as d_dept_id, e.department_id as e_dept_id, e.last_name
from shared_table2 d full outer join table_c1 e
                                   on d.department_id = e.department_id
order by d.department_id, e.last_name;

-- https://github.com/JSQLParser/JSqlParser/blob/f922e0d8efee673fa1d9176d1d4421b549be09ff/src/test/resources/net/sf/jsqlparser/statement/select/oracle-tests/join03.sql
select d.department_id, e.last_name
from m.table_c2 d right outer join n.shared_table3 e
                                      on d.department_id = e.department_id
order by d.department_id, e.last_name;
