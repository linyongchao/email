email：邮件发送功能封装

依赖：该功能基于mail.jar封装，故需要引入mail.jar

使用方法：  
1. 下载项目，打包成jar包  
2. 在目标项目中引入mail.jar和步骤1打成的jar包  
3. 在目标项目src目录下新建email.properties，配置属性  
username=youremail@163.com  
password=pwd  
smtp=smtp.163.com  
defaultTitle=emailDefaultTitle  
emailName=emailName  
前三个为必须属性，后两个为可选属性  
4. 在目标项目中调用：EmailService.send()方法即可  

下一步：  
该项目为初级版本，接下来要实现的功能有：  
1. 发送给多人  
2. 抄送和密送  
3. 优化send方法  

====================================================

前几天偶然发现16年有人通过issues给我点赞了  
说实话，触动挺大的  
完全没有想到这么古老的代码还有价值  
正好公司内也要用邮件发送，公私两便  
就重构了一版放在com.refactoring.email包下，实现了第一版未完成的下一步目标  
使用方法见Test类  
希望能帮到更多人吧
