#
# Template to customise msr based image
#

FROM store/softwareag/webmethods-microservicesruntime:10.5.0.3-alpine
LABEL MAINTAINER="jcart@mac.com" \
	DESCRIPTION="Example for merging webMethods MSR with your own packages" \
	COMMENT="first release" \
	BUILD-TEMPLATE="MSR" \
	TYPE="Micro Service" 		

EXPOSE 5555

USER sagadmin	

# Add dependent packages
			
	ADD --chown=sagadmin ./JcPublicTools /opt/softwareag/IntegrationServer/packages/JcPublicTools			

# add YOUR packages here -->



			
			
			

