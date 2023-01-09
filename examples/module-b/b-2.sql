-- https://github.com/JSQLParser/JSqlParser/blob/f922e0d8efee673fa1d9176d1d4421b549be09ff/src/test/resources/net/sf/jsqlparser/statement/select/oracle-tests/cast_multiset01.sql
select t1.department_id, t2.*
from shared_table2 t1,
     table
         (
             cast
                 (
                     multiset
                         (
                         select t3.last_name, t3.department_id, t3.salary
                         from shared_table3 t3
            where t3.department_id = t1.department_id
                         )
                 as people_tab_typ
                 )
         ) t2
