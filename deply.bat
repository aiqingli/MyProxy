mvn -Dmaven.test.skip=true clean install && xcopy "feature1\target\feature1-0.0.1.war" "C:\ali\dev\tomcat8\webapps\sp#feature1.war" && xcopy "feature2\target\feature2-0.0.1.war" "C:\ali\dev\tomcat8\webapps\feature2.war" && xcopy "sp\target\sp-0.0.1.war" "C:\ali\dev\tomcat7\webapps\sp.war"