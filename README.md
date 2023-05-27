# consul
consul hello world





# 1. consul ?

Consul 은 key value 형식의 저장소

각종 config 파일들을 저장해놓고 클러스터 관리에 사용시 용이하다.





# 2. consul 실행 with Docker



## 1) docker run

```sh

 $ docker run --rm -d --name consul -p 8500:8500 consul
 
 
```



## 2) key/value 입력 및 학인



```sh

# key/value 입력

# key : 
/nginx/loadbalancing.json

# value :
hello world


$ curl http://localhost:8500/v1/kv/nginx/loadbalancing.json?raw
hello world

```



## 3) List keys

```sh

{CONSUL_URL}:{PORT}/v1/kv?raw&keys


$ curl http://localhost:8500/v1/kv/nginx?raw&keys
hello world



$ curl http://localhost:8500/v1/kv?raw&keys
hello world


```



## 5) Clean Up

```sh
$ docker rm -f consul
```









# 3. Install with helm

참고링크  : https://developer.hashicorp.com/consul/docs/k8s/installation/install



## 1) helm reopo

```sh


$ helm repo add hashicorp https://helm.releases.hashicorp.com
 "hashicorp" has been added to your repositories
 
 

$ helm repo list
NAME            URL
bitnami         https://charts.bitnami.com/bitnami
hashicorp       https://helm.releases.hashicorp.com

 
$ helm search repo hashicorp/consul
NAME                CHART VERSION   APP VERSION DESCRIPTION
hashicorp/consul        1.1.1           1.15.1          Official HashiCorp Consul Chart



# helm chart fetch
$ mkdir -p ~/yjsong/helm/charts
$ cd ~/yjsong/helm/charts

$ helm fetch hashicorp/consul

$ ll
-rw-r--r-- 1 ktdseduuser ktdseduuser 105419 May 20 06:28 consul-1.1.1.tgz

$ tar -xzvf consul-1.1.1.tgz
$ cd consul


# consul 설치

# namespace 생성
$ kubectl create ns consul


 
```





## 2) consul install



```sh

# consul 설치
$ cd ~/yjsong/helm/charts/consul

# dry-run
$ helm -n consul install consul . \
    --set global.name=consul \
    --dry-run=true > dry-run01.yaml

$ helm -n consul install consul hashicorp/consul \
    --set global.name=consul



# dry-run
$ helm -n consul install consul . \
    --set global.name=consul \
    --set connectInject.enalbed=false \
    --set meshGateway.enalbed=false \
    --set ingressGateways.enalbed=false \
    --set apiGateway.enalbed=false \
    --set externalServers.enalbed=false \
    --dry-run=true > dry-run02.yaml


# install
$ helm -n consul install consul . \
    --set global.name=consul \
    --set connectInject.enalbed=false \
    --set meshGateway.enalbed=false \
    --set ingressGateways.enalbed=false \
    --set apiGateway.enalbed=false \
    --set externalServers.enalbed=false \
    --dry-run=true > dry-run02.yaml






$ kubectl -n consul get all
NAME                                               READY   STATUS    RESTARTS   AGE
pod/consul-connect-injector-7bf6759dfd-7qncm       1/1     Running   0          38m
pod/consul-server-0                                1/1     Running   0          38m
pod/consul-webhook-cert-manager-5dd6848777-nnxqp   1/1     Running   0          38m

NAME                              TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)                                                                            AGE
service/consul-connect-injector   ClusterIP   10.43.92.60     <none>        443/TCP                                                                            38m
service/consul-dns                ClusterIP   10.43.200.118   <none>        53/TCP,53/UDP                                                                      38m
service/consul-server             ClusterIP   None            <none>        8500/TCP,8502/TCP,8301/TCP,8301/UDP,8302/TCP,8302/UDP,8300/TCP,8600/TCP,8600/UDP   38m
service/consul-ui                 ClusterIP   10.43.154.120   <none>        80/TCP                                                                             38m

NAME                                          READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/consul-connect-injector       1/1     1            1           38m
deployment.apps/consul-webhook-cert-manager   1/1     1            1           38m

NAME                                                     DESIRED   CURRENT   READY   AGE
replicaset.apps/consul-connect-injector-7bf6759dfd       1         1         1       38m
replicaset.apps/consul-webhook-cert-manager-5dd6848777   1         1         1       38m

NAME                             READY   AGE
statefulset.apps/consul-server   1/1     38m



$ kubectl get crd
NAME                                       CREATED AT
addons.k3s.cattle.io                       2023-05-20T03:28:05Z
exportedservices.consul.hashicorp.com      2023-05-20T06:51:22Z
helmchartconfigs.helm.cattle.io            2023-05-20T03:28:05Z
helmcharts.helm.cattle.io                  2023-05-20T03:28:05Z
ingressgateways.consul.hashicorp.com       2023-05-20T06:51:22Z
ingressroutes.traefik.containo.us          2023-05-20T03:28:36Z
ingressroutetcps.traefik.containo.us       2023-05-20T03:28:36Z
ingressrouteudps.traefik.containo.us       2023-05-20T03:28:36Z
meshes.consul.hashicorp.com                2023-05-20T06:51:22Z
middlewares.traefik.containo.us            2023-05-20T03:28:36Z
middlewaretcps.traefik.containo.us         2023-05-20T03:28:36Z
proxydefaults.consul.hashicorp.com         2023-05-20T06:51:22Z
serverstransports.traefik.containo.us      2023-05-20T03:28:36Z
servicedefaults.consul.hashicorp.com       2023-05-20T06:51:22Z
serviceintentions.consul.hashicorp.com     2023-05-20T06:51:22Z
serviceresolvers.consul.hashicorp.com      2023-05-20T06:51:22Z
servicerouters.consul.hashicorp.com        2023-05-20T06:51:22Z
servicesplitters.consul.hashicorp.com      2023-05-20T06:51:22Z
terminatinggateways.consul.hashicorp.com   2023-05-20T06:51:22Z
tlsoptions.traefik.containo.us             2023-05-20T03:28:36Z
tlsstores.traefik.containo.us              2023-05-20T03:28:36Z
traefikservices.traefik.containo.us        2023-05-20T03:28:36Z



# ingress 생성
$ ku create -f ./kubernetes/userlist/16.userlist-ingress-cloud.yaml








```



## 3) 주요 선택항목

```

# 주요 선택항목

# Configures the automatic Connect sidecar injector.
connectInject:


# Configuration settings for the Consul API Gateway integration
apiGateway:

# Values that configure running a Consul client on Kubernetes nodes.
client:


```







## 4) ingress



```sh


## ingress
$ cd ~/yjsong/consul

# ingress 확인
$ cat > ./15.consul-ingress-cloud.yaml

---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: consul-ingress
  annotations:
    kubernetes.io/ingress.class: "traefik"
spec:
  rules:
  - host: "consul.cloud.35.209.207.26.nip.io"
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: consul-server
            port:
              number: 8500
---


$ kubectl -n consul apply -f ./15.consul-ingress-cloud.yaml


$ kubectl -n consul get ingress
NAME             CLASS    HOSTS                               ADDRESS                               PORTS   AGE
consul-ingress   <none>   consul.cloud.35.209.207.26.nip.io   10.128.0.35,10.128.0.36,10.128.0.38   80      3s

```





## 5) clean up



```sh
$ cd ~/yjsong/consul
$ kubectl -n consul delete -f ./15.consul-ingress-cloud.yaml

```







# 4. Spring boot - configuration



참조 : https://docs.spring.io/spring-cloud-consul/docs/current/reference/html/#spring-cloud-consul-config



Consul은 구성 및 기타 메타데이터를 저장하기 위한 키/값 저장소를 제공한다.

Spring Cloud Consul Config는 Config Server 및 Client의 대안입니다 . 

구성은 특별한 "부트스트랩" 단계 동안 Spring 환경으로 로드됩니다. 

이러한 환경 구성 기본적으로 `/config` 폴더에 저장된다.

예를 들어 이름이 "testApp"이고 프로필이 "dev"인 애플리케이션에는 다음과 같은 속성 소스가 생성된다.

```
config/testApp,dev/ 
config/testApp/ 
config/application,dev/ 
config/application/
```





## 1) Simple Consul Source

참고 : https://www.youtube.com/watch?v=e2dJlRFWJMQ

### (1) spring boot  starter



아래 3개를 포함하여 프로젝트 를 생성하자.

```

name : spring-boot-consul

dependency: 
			spring-web
			...
			spring-boot-devtools
			...
			spring-cloud-starter-consul-config
			...
			spring-boot-configuration-processor
			...

```



### (2) 소스

MyConfig.java

```java

@Component
@RefreshScope
public class MyConfig {
	
	@Value("${dataMessage}")
	private String dataMessage;

	public String getDataMessage() {
		return dataMessage;
	}

	public void setDataMessage(String dataMessage) {
		this.dataMessage = dataMessage;
	}

}
```



SpringBootConsulApplication.java

```java
@SpringBootApplication
@RestController
public class SpringBootConsulApplication {
	
	@Autowired
	private MyConfig config;
	
	@GetMapping("/getConfigData")
	public String getConfiguration() {
		return config.getDataMessage();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootConsulApplication.class, args);
	}

}

```





### (3) application.yml

Spring Boot 2.4는 spring.config.import 속성을 통해 config data 를 가져오도록 변경되었다

application.properties

```yaml
spring.config.import=optional:consul:
```



아래처럼 설정한다.

```yaml
spring:
  config:
    import: "consul:"
  cloud:
    consul:
      config:
        enabled: true
        name: ssongman
        # fail-fast: true
        # import-check.enabled: false
      host: localhost
      port: 8500
  application:
    name: ssongman
server:
  port: 8181
  
```



consul 을 포함후 consul 설정이 마무리되지않고 최초 실행시 오류발생하므로 아래 `enabled: false` 옵션으로 수행한다.

```yaml
spring:
  cloud:
    consul:
      config:
        enabled: false
```







### (4) Consul 설정

localhost:8500 으로 접속후



```sh

# key/value 입력

# key : 
/config/ssongman/dataMessage

# value :
Hello Spring boot consul

# 확인
$ curl http://localhost:8500/v1/kv/config/ssongman/dataMessage?raw
Hello Spring boot consul



```







### (5) Spring boot 실행 및 확인

localhost:8181 으로 접속후



```sh
# key/value 입력

# key : 
/config/ssongman/dataMessage

# value :
Hello Spring boot consul

# 확인
$ curl http://localhost:8181/getConfigData
Hello Spring boot consul                       

# <-- 성공

# consul 에서 값을 변경후 테스트 해보자.
# 동적으로 값이 변경 된다.


```





## 2) watch









## 3) git2consul

git2consul은 git 저장소에서 개별 키로 파일을 Consul로 로드하는 Consul 커뮤니티 프로젝트이다.

기본적으로 키 이름은 파일 이름이다.

YAML 및 속성 파일은 각각 .yml 및 .properties 파일 확장자로 지원된다.  spring.cloud.consul.config.format 속성을 FILES로 설정합니다. 예를 들어:

*bootstrap.yml*

```yaml
spring:
  cloud:
    consul:
      config:
        format: FILES
```





# 5. Service Discovery with Consul

서비스 검색은 마이크로서비스 기반 아키텍처의 핵심 원칙 중 하나입니다.

 각 클라이언트 또는 일부 형태의 규칙을 수동으로 구성하는 것은 수행하기가 매우 어려울 수 있으며 매우 취약할 수 있습니다.

Consul은 HTTP API 및 DNS를 통해 Service Discovery 서비스를 제공합니다. Spring Cloud Consul은 서비스 등록 및 검색을 위해 HTTP API를 활용합니다. 이는 비 Spring Cloud 애플리케이션이 DNS 인터페이스를 활용하는 것을 막지 않습니다. Consul Agents 서버는 가십 프로토콜을 통해 통신하고 Raft 합의 프로토콜을 사용하는 클러스터에서 실행됩니다.









아래 3개를 포함하여 프로젝트 를 생성하자.

```
name : spring-boot-consul

dependency: 
			spring-web
			...
			spring-boot-devtools
			...
			spring-cloud-starter-consul-discovery
			...

```







# 9. STS 설치

링크 

https://download.springsource.com/release/STS4/4.18.1.RELEASE/dist/e4.27/spring-tool-suite-4-4.18.1.RELEASE-e4.27.0-win32.win32.x86_64.self-extracting.jar