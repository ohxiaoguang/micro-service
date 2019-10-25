异构微服务架构体验DEMO
> 跨语言服务调用，Jenkins CICD，自动部署Kubernetes集群

### 技术栈：
1. Java, Python,
2. SpringBoot, MyBatis, Dubbo, Thrift, Zookeeper, Mysql, Redis
3. Docker, GitLab, Jenkins, Kubernetes

### 主要说明
> 本 Demo 旨在体验跨语言间的服务调用和 K8s 相关技术，所以涉及业务较为简单，用户验证也只是简单实现，并未使用JWT、OAuth等认证机制进行开发，只可用作Demo。主要如下

1. 使用 Python 开发 message-service 服务，实现用户注册邮箱验证的功能，并将验证码存在 Redis 用于注册验证。

2. 使用 SpringBoot + MyBatis 开发 user-service 和 course-service 服务。
user-service 服务实现用户登录注册功能，用户登录后会生成 token 令牌，发送给用户并存于 Redis 中，用于后续用户请求的身份验证。
course-service 服务实现了课程列表功能，用户请求接口时需要携带 token 令牌，验证通过才可获取数据。

3. 用户服务与课程服务间通信使用 Dubbo + Zookeeper 实现，用户服务与信息服务直接通过 Thrift 通信。

4. 开发完成后编写 DockerFile 文件和生成 Docker 镜像的 shell 脚本。
编写 Jenkins Pipeline 流水线脚本，实现自动构建镜像，自动部署等操作。
上传代码到 GirLab 仓库，触发 Jenkins 脚本，自动构建 Docker 镜像上传到 Harbor 仓库，并自动部署到 Kubernetes 集群中。

