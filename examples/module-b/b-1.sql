-- https://github.com/JSQLParser/JSqlParser/blob/f922e0d8efee673fa1d9176d1d4421b549be09ff/src/test/resources/net/sf/jsqlparser/statement/select/oracle-tests/case_when01.sql
select
    ROOT,LEV,OBJ,LinK,PaTH,cycle,
    case
        when (LEV - LEaD(LEV) over (order by orD)) < 0 then 0
        else 1
        end is_LEaF
from shared_table1
