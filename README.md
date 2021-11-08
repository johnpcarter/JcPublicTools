# JcPublicTools

A complimentary set of services to use with your webMethods runtime to develop integration faster.

Service documentation is available in Designer via the comments of each service.

**Installation**

This source code is a webMethods Integration/Micro Service Runtime package and you will need to first install this or download a docker image.

**local installation**

If you have an Integration Server or Micro Service Runtime running locally for development purposes, first navigate to your packages directory;

*$cd /${SAG_HOME}/IntegrationServer/packages*  
or
*$cd /${SAG_HOME}/IntegrationServer/instances/${INSTANCE}/packages*  

If your packages directory is already under version control

*$git submodule add https://github.com/johnpcarter/JcPublicTools.git JcPublicTools*  

or if you are not, then simply clone the repository

*$git clone https://github.com/johnpcarter/JcPublicTools.git*  

Then restart your runtime server and refresh your package browser in Designer.

**Docker Installation**

A predefined Dockerfile template has been provided in the resources directory. It is recommended that you copy the directory
and then update Dockerfile and aclmap_sm.cnf file appropriately.

cd into your directory and download the latest source code

*$ cd ${WORKING_DIR}*  

*$ git clone https://github.com/johnpcarter/JcPublicTools.git*  

You will also need to add your own packages and configuration as part of the build by copying the following line into the section 'add YOUR packages here'
e.g.

*ADD --chown=sagadmin ./c8yPhilipsHueAgent /opt/softwareag/IntegrationServer/packages/c8yPhilipsHueAgent*  

and also update the ./resources/aclmap_sm.cnf file to include permissions for any the services that you are exposing through an API.

You could also choose to include your MSR license file by adding the following line

*ADD --chown=sagadmin .licenseKey.xml /opt/softwareag/IntegrationServer/config/licenseKey.xml*  

You can now build your image

*$ docker build -t ${YOUR DOCKER IMAGE} .*  
