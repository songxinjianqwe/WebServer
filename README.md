# 自制HTTP服务器（包括HTTP服务器和Servlet容器）
## 具备的功能(均为简化版的实现)：

- HTTP Protocol
- Servlet
- ServletContext
- Request
- Response
- Dispatcher
- Static Resources & File Download
- Error Notification
- Get & Post & Put & Delete
- web.xml parse
- Forward
- Redirect
- Simple TemplateEngine
- session
- cookie

## 使用技术

基于Java BIO、多线程、Socket网络编程、XML解析、log4j/slf4j日志
只引入了junit,lombok(简化POJO开发),slf4j,log4j,dom4j(解析xml),mime-util(用于判断文件类型)依赖，与web相关内容全部自己完成。

## 未来希望添加的功能：
- NIO实现多路复用
- 手写WebSocket服务器，实现HTTP长连接
- Filter
- Listener


## 另附CSDN相关博客
http://blog.csdn.net/songxinjianqwe/article/details/75670552