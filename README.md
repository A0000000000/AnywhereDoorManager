# AnywhereDoorManager

## AnywhereDoor后台管理
* 管理Plugin信息
* 管理Imsdk信息

## RESTful接口

### User相关
1. 注册
   * GET /user/register
   * params:
     * username: 用户名 QueryString
     * password: 密码 QueryString
   * result:
     * success: 返回token及flush_token
     * failed: 返回具体错误类型
2. 登录
   


## 部署方式
1. 将代码仓库clone到本地
2. 安装docker及buildx
3. 打包镜像: 
    * `docker buildx build --platform linux/amd64 -t 192.168.25.5:31100/maoyanluo/anywhere-door-manager-test:1.0 . --load`
4. 创建容器:
    * `docker run --name anywhere-door-manager -itd -p 8080:80 -e DB_IP=192.168.25.7 -e DB_PORT=3306 -e DB_NAME=anywhere_door -e DB_USER=root -e DB_PASSWORD=09251205 192.168.25.5:31100/maoyanluo/anywhere-door-manager:1.0`