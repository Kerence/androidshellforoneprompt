一般来说 执行root权限的shell脚本方法是
<br/>
`Process process = Runtime.getRuntime().exec("sh");`<br/>
`OutputStream os = new DataOutputStream(process.getOutputStream());`<br/>
`os.writeBytes(command + "\n");`<br/>
`os.writeBytes("exit\n");`<br/>
`os.flush();`<br/>
`process.waitFor();`<br/>
<br/>
存在的问题是每次执行时都会提示“已经授予"xxx"超级用户权限"
解决方案
使用开源的android terminal simulator 进行改造，封装成了简单的api来执行，
在application或者activity中初始化
TermSessionCommandUtil.getInstance(context);
用下面的代码执行命令
TermSessionCommandUtil.getInstance(context).exec("screencap -p /sdcard/a.png");
这样就不会反复显示已经授予"xxx'超级用户权限的提示了
