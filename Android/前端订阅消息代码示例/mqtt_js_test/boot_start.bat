rem *******************************Code Start*****************************
rem *******************************windows 开机自启动activeMq消息代理服务，需要将此脚本放入系统启动目录中*****************************

@echo off

echo ----------------start to boot artemis---------

cmd /k "cd /d E:\debug\artemis_broker\bin\ && artemis.cmd run"


@echo on

rem ***************************Code End*****************************