
DROP PROCEDURE IF EXISTS `Utility_GetRegularDateVal`; -- 注意存储名不加引号。
delimiter $$
CREATE PROCEDURE `Utility_GetRegularDateVal`
	(IN i_seqname varchar(32),
    IN i_length int,
    IN i_currdate varchar(10),
    IN i_step int)
BEGIN
	DECLARE  p_cdatetime		varchar(50);
	DECLARE  p_stepValue		int;
	DECLARE  p_curentVal		int;
	DECLARE  p_preFixString	varchar(50);
	DECLARE  p_postFixString	varchar(50);
	DECLARE  p_dataTimeString	varchar(50);
    DECLARE  p_test	varchar(500);

	-- 按照当前日期获取流水号
	select PreFixString, PostFixString into p_preFixString, p_postFixString from `sys_SysSequence` where SequenceName = i_seqname for update;
	select CurrentValue, StepValue into p_curentVal, p_stepValue from `sys_SysSequence` where SequenceName = i_seqname and CurrDate = i_currdate;
	set p_cdatetime = replace(i_currdate,'-','');

	if i_step > 0 THEN
		set p_stepValue = i_step;
	end if;

	if p_curentVal  is null THEN
		start transaction;
			update `sys_SysSequence` set CurrentValue = p_stepValue, CurrDate = i_currdate where SequenceName = i_seqname;
			if(p_postFixString is null) THEN
				select uuid() as Id, i_seqname as SeedType,  concat(p_preFixString, p_cdatetime, right(concat(REPEAT('0',i_length), '1'), i_length)) as SeedValue,  1 as SeedMin,  p_stepValue as SeedMax;
			else
				select uuid() as Id, i_seqname as SeedType, concat(p_preFixString, p_cdatetime, right(concat(REPEAT('0',i_length), '1'), i_length), p_postFixString) as SeedValue, 1 as SeedMin, p_stepValue as SeedMax;
            end if;
		commit;
	else
		start transaction;
			update `sys_SysSequence` set CurrentValue = p_curentVal + p_stepValue  where SequenceName = i_seqname;
			if p_postFixString is null THEN
				select uuid() as Id, i_seqname as SeedType, concat(p_preFixString, p_cdatetime, right(concat(REPEAT('0',i_length), cast((p_curentVal + p_stepValue) as char)), i_length)) as SeedValue, p_curentVal + 1 as SeedMin, p_curentVal + p_stepValue as SeedMax;
			else
				select uuid() as Id, i_seqname as SeedType, concat(p_preFixString, p_cdatetime, right(concat(REPEAT('0',i_length), cast((p_curentVal + p_stepValue) as char)), i_length), p_postFixString) as SeedValue, p_curentVal + 1 as SeedMin, p_curentVal + p_stepValue as SeedMax;
			end if;
		commit;
	end if;
	#select o_SeedType as SeedType, o_SeedValue as SeedValue, o_SeedMin as SeedMin, o_SeedMax as SeedMax;
END$$

