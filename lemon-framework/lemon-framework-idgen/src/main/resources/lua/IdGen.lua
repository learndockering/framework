--KEYS[1] key
--KEYS[2] hashKey
--ARGV[1] maxValue
--ARGV[2] delta

local existRdsVal = redis.call('HEXISTS',KEYS[1],KEYS[2])

if existRdsVal == 0 then
    redis.call('HSET',KEYS[1],KEYS[2],ARGV[2])
    redis.log(redis.LOG_WARNING, "not exist redis value for key="..KEYS[1]..",hkey="..KEYS[2])
    return ARGV[2]
end

if tonumber(ARGV[1]) == -1 then
   --redis.log(redis.LOG_WARNING,"max value equals -1.")
   return redis.call('HINCRBY',KEYS[1],KEYS[2],ARGV[2])
end
 
local rdsVal = redis.call('hget',KEYS[1],KEYS[2])

--redis.log(redis.LOG_WARNING,"redis value is "..rdsVal..", maxValue is "..ARGV[1])

if ( tonumber(ARGV[1]) < tonumber(rdsVal) ) then
    --redis.log(redis.LOG_WARNING,"maxValue "..ARGV[1].." less than redis value "..rdsVal)
    redis.call('HSET',KEYS[1],KEYS[2],ARGV[2])
    return ARGV[2]
else
    --redis.log(redis.LOG_WARNING,"maxValue "..ARGV[1].."large than redis value "..rdsVal)
    return redis.call('HINCRBY',KEYS[1],KEYS[2],ARGV[2])
end

