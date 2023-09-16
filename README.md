
# TAP
```bash
tanzu apps workload create sampleapp -f ./workload.yaml --type server-with-volumes --label app.kubernetes.io/part-of=sampleapp --label apps.tanzu.vmware.com/has-tests="true" --yes --namespace tap-demo
```

# docker で動作確認

```bash
rm -rf target
mvn package

# イメージを全削除したい場合のみ
docker images | grep g2app | awk '{ print $1 }' | xargs docker rmi

docker build --tag=g2app .
docker run --rm -p 8080:8080 -p 9990:9990 -it g2app /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
curl http://localhost:8080/g2app/function01
```


