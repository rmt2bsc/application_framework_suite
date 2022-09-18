rem RMT2 FW Core Configuration binding...

del /Q ..\..\java\com\api\config\jaxb\*.*
xjc -d ../../java/ -p com.api.config.jaxb ../../../test/resources/xml/RMT2AppServerConfig.xsd