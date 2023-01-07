var app = angular.module("myApp", []);
app.filter("orderObjectBy", function () {
  return function (input, attribute) {
    const ordered = Object.keys(input)
      .sort()
      .reduce((obj, key) => {
        obj[key] = input[key];
        return obj;
      }, {});
    return ordered;
  };
});
app.controller("myCtrl", function ($scope, myserv) {
  $scope.isAuthenticated = false;
  $scope.url = location.origin;
  $scope.services = myserv.getServiceList();
  $scope.lastUpdated = myserv.getCreatedTime();
  $scope.apis = myserv.getAPIList();
  $scope.apiMap = new Map();
  $scope.serviceMap = new Map();
  $scope.apiMap = new Map();
  $scope.schemes = new Map();
  $scope.ioMap = new Map();
  $scope.package = myserv.getPackageInfo();

  $scope.showReload = false;

  $scope.changeDisplay = function (className, value) {
    var x = document.getElementsByClassName(className);
    var i;
    for (i = 0; i < x.length; i++) {
      x[i].style.display = value;
    }
  };

  $scope.removeElements = function (className) {
    var x = document.getElementsByClassName(className);
    var i;
    for (i = 0; i < x.length; i++) {
      x[i].remove();
    }
  };

  $scope.addAuthorizedDetails = function(){
    var version=document.getElementById("version")
    var description=document.getElementById("description")
    if(document.getElementById("buildnumber")==null&!($scope.package.buildNumber==undefined||$scope.package.buildNumber==null||$scope.package.buildNumber.trim()==''))
      version.insertAdjacentHTML("afterend", "<tr id='buildnumber' ng-if=''><td style='width:20%;color:grey;'>Build Number</td><td class='col-auto'>"+$scope.package.buildNumber+"</td></tr>");
    if(document.getElementById("createddate")==null&!($scope.package.createdDate==undefined||$scope.package.createdDate==null||$scope.package.createdDate.trim()==''))
      description.insertAdjacentHTML("afterend",  "<tr id='createddate' ng-if=''><td style='width:20%;color:grey;'>Created On</td><td class='col-auto'>"+$scope.package.createdDate+"</td></tr>");
    if(document.getElementById("status")==null&&$scope.package.status!=undefined&&$scope.package.status!=null&&$scope.package.status.trim()!='')
      document.getElementById("packagedetails").innerHTML += "<tr id='status' ng-if=''><td style='width:20%;color:grey;'>Status</td><td class='col-auto text-truncate' data-toggle='tooltip' data-placement='bottom' title='"+$scope.package.status+"'>"+$scope.package.status+"</td></tr>";
    if(document.getElementById("tags")==null&&$scope.package.tags!=undefined&&$scope.package.tags!=null&&$scope.package.tags.join('').trim()!=''){
      document.getElementById("packagedetails").innerHTML += "<tr id='tags' ng-if=''><td style='width:20%;color:grey;'>Tags</td><td class='col-auto text-truncate' data-toggle='tooltip' data-placement='bottom' title='"+$scope.package.tags.join(',')+"'>"+$scope.package.tags.join(',')+"</td></tr>";
    }
    var apiList = document.getElementById("apis").getElementsByClassName("dlt-accordion-item");
    for(j=0;j<apiList.length;j++){
      if($scope.apiMap.has(`api${j}`)){
        var apiElement = apiList[j].getElementsByTagName("button")[0];
        document.getElementById(`api${j}`).innerHTML = document.getElementById(`api${j}`).innerHTML.replace(apiElement.getAttribute("title"),$scope.apiMap.get(`api${j}`));
        document.getElementById(`api${j}`).setAttribute("title",$scope.apiMap.get(`api${j}`));
        if($scope.schemes.get(`apibody${j}`)!=undefined&&apiList[j].getElementsByClassName("scheme-container").length==0){
          const div = document.createElement("div");
          div.classList.add("scheme-container");
          div.insertAdjacentHTML('beforeend',$scope.schemes.get(`apibody${j}`).innerHTML)
          document.getElementsByClassName("information-container")[j].appendChild(div)
      }
    }
    }
    var servicesList = document.getElementById("services").getElementsByClassName("dlt-accordion-item");
    for(j=0;j<servicesList.length;j++){
      var service = servicesList[j].getElementsByTagName("button")[0];
      document.getElementById(`service${j}`).innerHTML = document.getElementById(`service${j}`).innerHTML.replace(service.getAttribute("title"),$scope.serviceMap.get(`service${j}`));
      document.getElementById(`service${j}`).setAttribute("title",$scope.serviceMap.get(`service${j}`));
    }
    for(i=0;i<servicesList.length;i++){
      var inputList = document.getElementById(`servicebody${i}`).getElementsByClassName("input-row");
      for(j=0;j<inputList.length;j++){
        var input = document.getElementById(`${i}${j}iname`);
        document.getElementById(`${i}${j}iname`).innerHTML = document.getElementById(`${i}${j}iname`).innerHTML.replace(input.getAttribute("title"),$scope.ioMap.get(`${i}${j}iname`));
        document.getElementById(`${i}${j}iname`).setAttribute("title",$scope.ioMap.get(`${i}${j}iname`));
      }
      var outputList = document.getElementById(`servicebody${i}`).getElementsByClassName("output-row");
      for(j=0;j<outputList.length;j++){
        var output = document.getElementById(`${i}${j}oname`);
        document.getElementById(`${i}${j}oname`).innerHTML = document.getElementById(`${i}${j}oname`).innerHTML.replace(output.getAttribute("title"),$scope.ioMap.get(`${i}${j}oname`));
        document.getElementById(`${i}${j}oname`).setAttribute("title",$scope.ioMap.get(`${i}${j}oname`));
      }
    }
}

  $scope.addLastUpdated = function(){
    if(document.getElementById("last-update")==null)
      document.getElementsByClassName("main-nav")[0].innerHTML +=  "<li id='last-update' class='ml-auto navbar-text mt-auto mb-auto pr-1' style='color:white;max-width:12rem;display: flex;'><span style='font-size: x-small;'>Last generated on<br>"+myserv.getCreatedTime()+"</span></li>";
  }

  $scope.removeRegenerate = function(){
    if(document.getElementById("navreload")!=null)
      document.getElementById("navreload").remove();
  }

  $scope.removeUnauthorized = function(){
    if(document.getElementById("navreload")!=null)
      document.getElementById("navreload").remove();
    if(document.getElementById("last-update")!=null)
      document.getElementById("last-update").remove();
    if(document.getElementById("buildnumber")!=null)
      document.getElementById("buildnumber").remove()
    if(document.getElementById("createddate")!=null)
      document.getElementById("createddate").remove()
    if(document.getElementById("status")!=null)
      document.getElementById("status").remove()
    if(document.getElementById("tags")!=null)
      document.getElementById("tags").remove()
    var schemes = document.getElementsByClassName("scheme-container");
    for(k=0;k<schemes.length;k++){
      $scope.schemes.set(document.getElementsByClassName("scheme-container")[k].parentNode.parentNode.parentNode.parentNode.parentElement.getAttribute("id"),document.getElementsByClassName("scheme-container")[k])
    }
    $scope.removeElements("scheme-container");
    var apiList = document.getElementById("apis").getElementsByClassName("dlt-accordion-item");
    for(j=0;j<apiList.length;j++){
      var apiElement = apiList[j].getElementsByTagName("button")[0];
      document.getElementById(`api${j}`).innerHTML = document.getElementById(`api${j}`).innerHTML.replace(apiElement.getAttribute("title"),apiElement.getAttribute("title").split(':').pop());
      document.getElementById(`api${j}`).setAttribute("title",apiElement.getAttribute("title").split(':').pop());
    }
    var servicesList = document.getElementById("services").getElementsByClassName("dlt-accordion-item");
    for(j=0;j<servicesList.length;j++){
      var service = servicesList[j].getElementsByTagName("button")[0];
      document.getElementById(`service${j}`).innerHTML = document.getElementById(`service${j}`).innerHTML.replace(service.getAttribute("title"),service.getAttribute("title").split(':').pop());
      document.getElementById(`service${j}`).setAttribute("title",service.getAttribute("title").split(':').pop());
    }
    for(i=0;i<servicesList.length;i++){
      var inputList = document.getElementById(`servicebody${i}`).getElementsByClassName("input-row");
      for(j=0;j<inputList.length;j++){
        var input = document.getElementById(`${i}${j}iname`);
        document.getElementById(`${i}${j}iname`).innerHTML = document.getElementById(`${i}${j}iname`).innerHTML.replace(/([a-zA-Z0-9_]+[\.])+/,'').replace(/([a-zA-Z0-9_]+)+:/,'');
        document.getElementById(`${i}${j}iname`).setAttribute("title",input.getAttribute("title").replace(/([a-zA-Z0-9_]+[\.])+/,'').replace(/([a-zA-Z0-9_]+)+:/,''));
      }
      var outputList = document.getElementById(`servicebody${i}`).getElementsByClassName("output-row");
      for(j=0;j<outputList.length;j++){
        var output = document.getElementById(`${i}${j}oname`);
        document.getElementById(`${i}${j}oname`).innerHTML = document.getElementById(`${i}${j}oname`).innerHTML.replace(/([a-zA-Z0-9_]+[\.])+/,'').replace(/([a-zA-Z0-9_]+)+:/,'');
        document.getElementById(`${i}${j}oname`).setAttribute("title",output.getAttribute("title").replace(/([a-zA-Z0-9_]+[\.])+/,'').replace(/([a-zA-Z0-9_]+)+:/,''));
      }
    }
  }

  $scope.addNavReload = function(){
    if(document.getElementById("navreload")==null){
      document.getElementsByClassName("main-nav")[0].innerHTML += "<li id='navreload' class='nav-item navbar-text' data-toggle='tooltip' data-placement='bottom' title='Regenerate the page'><button class='dlt-button-icon' style='float:right;' id='reload'><i class='dlt-icon-refresh'></i></button></li>";
        $("#navreload").on("click", function (ev) {
            ev.preventDefault();
            fetch(
              `${$scope.url}/invoke/pub.packages/updateHomePage?packageName=${$scope.package.packageName}&replaceHTML=false`,
              {
                headers: {
                  Accept: "application/json",
                  "Content-Type": "application/json",
                },
              }
            )
              .then((response) => {
                if (response["status"] === 200) {
                  response.json().then(function (text) {
                    if (text["success"]) {
                      var node = document.createElement("div");
                      node.innerHTML =
                        '<div class="title-wrapper"><span class="dlt-icon-success success-icon"></span><div class="toast-title">Success</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">' +
                        text["message"] +
                        "</div>";
                      node.classList.add("dlt-toast");
                      node.classList.add("success-border");
                      node.style.marginLeft = "auto";
                      node.style.position = "fixed";
                      node.style.top = "75px";
                      node.style.right = "25px";
                      document.getElementsByTagName("body")[0].appendChild(node);
                      document.getElementsByClassName("toast-message")[0].innerHTML =
                        text["message"];
                      document.getElementsByClassName("dlt-toast")[0].style.display =
                        "block";
                      setTimeout(function () {
                        location.reload();
                      }, 2000);
                    } else {
                      var node = document.createElement("div");
                      node.innerHTML =
                        '<div class="title-wrapper"><span class="dlt-icon-error error-icon"></span><div class="toast-title">Error</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">The server is not responding. Try again later.</div>';
                      node.classList.add("dlt-toast");
                      node.classList.add("error-border");
                      node.style.marginLeft = "auto";
                      node.style.position = "fixed";
                      node.style.top = "75px";
                      node.style.right = "25px";
                      document.getElementsByTagName("body")[0].appendChild(node);
                      document.getElementsByClassName("toast-message")[0].innerHTML =
                        text["message"];
                      document.getElementsByClassName("dlt-toast")[0].style.display =
                        "block";
                    }
                  });
                } else {
                  var node = document.createElement("div");
                    response.json().then((text) =>{
                    node.innerHTML =
                      '<div class="title-wrapper"><span class="dlt-icon-error error-icon"></span><div class="toast-title">Error</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">The server is not responding. Try again later.</div>';
                    node.classList.add("dlt-toast");
                    node.classList.add("error-border");
                    node.style.marginLeft = "auto";
                    node.style.position = "fixed";
                    node.style.top = "75px";
                    node.style.right = "25px";
                    document.getElementsByTagName("body")[0].appendChild(node);
                    document.getElementsByClassName("toast-message")[0].innerHTML =
                    text['$error'] + " - Unable to update the package home page.";
                    document.getElementsByClassName("dlt-toast")[0].style.display =
                      "block";
                    if(text['$error'].indexOf('Cannot modify the system package resources')!=-1){
                      $scope.removeRegenerate();
                    }else if (response["status"] === 401) {
                      $scope.removeUnauthorized();
                      $scope.isAuthenticated = false;
                    }}).catch((error)=>{
                      console.log(response)
                      node.innerHTML =
                      '<div class="title-wrapper"><span class="dlt-icon-error error-icon"></span><div class="toast-title">Error</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">The server is not responding. Try again later.</div>';
                      node.classList.add("dlt-toast");
                      node.classList.add("error-border");
                      node.style.marginLeft = "auto";
                      node.style.position = "fixed";
                      node.style.top = "75px";
                      node.style.right = "25px";
                      document.getElementsByTagName("body")[0].appendChild(node);
                      document.getElementsByClassName("toast-message")[0].innerHTML =
                      response['statusText'] + " - Unable to update the package home page.";
                      document.getElementsByClassName("dlt-toast")[0].style.display =
                        "block";
                      if (response["status"] === 401) {
                        $scope.removeUnauthorized();
                        $scope.isAuthenticated = false;
                      }
                    })
                }
              })
              .catch((error) => {
                var node = document.createElement("div");
                node.innerHTML =
                  '<div class="title-wrapper"><span class="dlt-icon-error error-icon"></span><div class="toast-title">Error</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">The server is not responding. Try again later.</div>';
                node.classList.add("dlt-toast");
                node.classList.add("error-border");
                node.style.marginLeft = "auto";
                node.style.position = "fixed";
                node.style.top = "75px";
                node.style.right = "25px";
                document.getElementsByTagName("body")[0].appendChild(node);
                document.getElementsByClassName("toast-message")[0].innerHTML =
                  "Unable to update the package home page. " + error;
                document.getElementsByClassName("dlt-toast")[0].style.display =
                  "block";
              });
          });
    }
   }

  $scope.initWindow = function(){
        hash = location.hash.replace(/^#/, "");
        if (isDownload) {

        } else {
          $scope.removeElements("download");
        }

    $("#homecontent").on("click", function (ev) {
      window.location.hash = "document";
      location.reload();
    });
    if ($(window).width() < 999) {
      document.getElementById("explore-content").style='left:10.55rem;top:5.5rem;';
      $(".dlt-panel__left").each(function () {
        document.getElementsByClassName("dlt-panel__left")[0].className +=
          " dlt-panel__collapsed";
        document.getElementById("about-text").style.display = "none";
        document.getElementById("service-text").style.display = "none";
        document.getElementById("api-text").style.display = "none";
      });
    } else {
      document.getElementById("explore-content").style='left:14.5rem;top:5.5rem;';
      document
        .getElementsByClassName("dlt-panel__left")[0]
        .classList.remove("dlt-panel__collapsed");
      document.getElementById("about-text").style.display = "block";
      document.getElementById("service-text").style.display = "block";
      document.getElementById("api-text").style.display = "block";
    }
    $(window).on("resize", function () {
      if ($(window).width() < 999) {
        document.getElementById("explore-content").style='left:10.55rem;top:5.5rem;';
        $(".dlt-panel__left").each(function () {
          document.getElementsByClassName("dlt-panel__left")[0].className +=
            " dlt-panel__collapsed";
          document.getElementById("about-text").style.display = "none";
          document.getElementById("service-text").style.display = "none";
          document.getElementById("api-text").style.display = "none";
        });
      } else {
        document.getElementById("explore-content").style='left:14.5rem;top:5.5rem;';
        document
          .getElementsByClassName("dlt-panel__left")[0]
          .classList.remove("dlt-panel__collapsed");
        document.getElementById("about-text").style.display = "block";
        document.getElementById("service-text").style.display = "block";
        document.getElementById("api-text").style.display = "block";
      }
    });
    var tothetop = document.getElementById("tothetop");

    window.onscroll = function () {
      scrollFunction();
    };

    function scrollFunction() {
      if (
        document.body.scrollTop > 20 ||
        document.documentElement.scrollTop > 20
      ) {
        tothetop.style.display = "block";
      } else {
        tothetop.style.display = "none";
      }
    }
    openLink = function (link) {
      window.open(link,"_blank");
    };
    backToTop = function () {
      document.body.scrollTop = 0;
      document.documentElement.scrollTop = 0;
    };
    hideText = function () {
      if (document.getElementsByClassName("dlt-panel__collapsed").length == 0) {
        document.getElementById("explore-content").style='left:10.55rem;top:5.5rem;';
        document.getElementById("about-text").style.display = "none";
        document.getElementById("service-text").style.display = "none";
        document.getElementById("api-text").style.display = "none";
      } else {
        document.getElementById("explore-content").style='left:14.5rem;top:5.5rem;';
        document.getElementById("about-text").style.display = "block";
        document.getElementById("service-text").style.display = "block";
        document.getElementById("api-text").style.display = "block";
      }
    };
    var hash = location.hash.replace(/^#/, ""); // ^ means starting, meaning only match the first hash
    var selectedTabId = "";
    if (hash) {
      if (hash == "service" || hash == "/service") {
        $('.nav-item a[href="#document"]').tab("show");
        $('.nav-item a[href="#' + hash + '"]').tab("show");
      } else if (hash == "api" || hash == "/api") {
        $('.nav-item a[href="#document"]').tab("show");
        $('.nav-item a[href="#' + hash + '"]').tab("show");
      } else if (hash == "about" || hash == "/about") {
        $('.nav-item a[href="#document"]').tab("show");
        $('.nav-item a[href="#' + hash + '"]').tab("show");
      } else {
        $('.nav-item a[href="#' + hash + '"]').tab("show");
      }
    }

    // Change hash for page-reload
    $(document).on('shown.bs.tab', ".nav-item a", function (e) {
      window.location.hash = e.target.hash;
      selectedTabId = e.target.id;
    });
    hash = location.hash.replace(/^#/, "");

    if (selectedTabId == "main-home-tab") {
      if(document.getElementById("last-update")!=null)document.getElementById("last-update").remove();
      if(document.getElementById("navreload")!=null)document.getElementById("navreload").remove();
    } else if (
      selectedTabId == "document-tab" ||
      selectedTabId == "about-tab" ||
      selectedTabId == "service-tab" ||
      selectedTabId == "api-tab"
    ) {
      if ($scope.isAuthenticated) {
        $scope.addLastUpdated();
        $scope.addNavReload();
        $scope.addAuthorizedDetails();
      } else {
        $scope.removeUnauthorized()
      }
    } else {
      if(document.getElementById("last-update")!=null)document.getElementById("last-update").remove();
      if(document.getElementById("navreload")!=null)document.getElementById("navreload").remove();
    }

    $scope.saveData = (function () {
      var a = document.createElement("a");
      document.body.appendChild(a);
      a.style = "display: none";
      return function (data, fileName) {
        var json = JSON.stringify(data),
          blob = new Blob([json], { type: "octet/stream" }),
          url = window.URL.createObjectURL(blob);
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
      };
    })();

    $(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function (e) {
      selectedTabId = e.target.id;
      hash = location.hash.replace(/^#/, "");
      if (hash == "main-home" || hash == "") {
        document.getElementById(`main-home-li`).style.borderTop =
          "4px solid #fff";
        document.getElementById(`main-home-li`).style.backgroundColor =
          "#14629f";
        document.getElementById(`main-home-li`).style.borderRight =
          "2px solid #3183de";
        document.getElementById(`main-home-li`).style.borderLeft =
          "2px solid #3183de";
        document.getElementById(`document-li`).style.borderTop = "none";
        document.getElementById(`document-li`).style.backgroundColor =
          "transparent";
        document.getElementById(`document-li`).style.borderRight = "none";
        document.getElementById(`document-li`).style.borderLeft = "none";
      } else if (
        hash == "document" ||
        hash == "about" ||
        hash == "service" ||
        hash == "api"
      ) {
        document.getElementById(`document-li`).style.borderTop =
          "4px solid #fff";
        document.getElementById(`document-li`).style.backgroundColor =
          "#14629f";
        document.getElementById(`document-li`).style.borderRight =
          "2px solid #3183de";
        document.getElementById(`document-li`).style.borderLeft =
          "2px solid #3183de";
        document.getElementById(`main-home-li`).style.borderTop = "none";
        document.getElementById(`main-home-li`).style.backgroundColor =
          "transparent";
        document.getElementById(`main-home-li`).style.borderRight = "none";
        document.getElementById(`main-home-li`).style.borderLeft = "none";
      } else {
        document.getElementById(`main-home-li`).style.borderTop =
          "4px solid #fff";
        document.getElementById(`main-home-li`).style.backgroundColor =
          "#14629f";
        document.getElementById(`main-home-li`).style.borderRight =
          "2px solid #3183de";
        document.getElementById(`main-home-li`).style.borderLeft =
          "2px solid #3183de";
        document.getElementById(`document-li`).style.borderTop = "none";
        document.getElementById(`document-li`).style.backgroundColor =
          "transparent";
        document.getElementById(`document-li`).style.borderRight = "none";
        document.getElementById(`document-li`).style.borderLeft = "none";
      }

      if (selectedTabId == "main-home-tab") {
        if(document.getElementById("last-update")!=null)document.getElementById("last-update").remove();
        if(document.getElementById("navreload")!=null)document.getElementById("navreload").remove();
      } else if (
        selectedTabId == "document-tab" ||
        selectedTabId == "about-tab" ||
        selectedTabId == "service-tab" ||
        selectedTabId == "api-tab"
      ) {
        if ($scope.isAuthenticated) {
          $scope.addLastUpdated();
          $scope.addNavReload();
          $scope.addAuthorizedDetails();
        } else {
          $scope.removeUnauthorized()
        }
      } else {
        if(document.getElementById("last-update")!=null)document.getElementById("last-update").remove();
        if(document.getElementById("navreload")!=null)document.getElementById("navreload").remove();
      }
    });

    $scope.requestFormat = {};
    $scope.responseFormat = {};
    $scope.defaultResponseFormat = {};

    downloadJson = function (element) {
      let elementName = $scope.apiMap.get(element.name);
      const options = {
        headers: {
          "Content-Disposition": "attachment",
          filename: `${elementName}.json`,
        },
      };
      var element = $scope.apis.filter((api) => api.name === elementName)[0];
      var url = "";
      if (element.spec.openapi != undefined) {
        url = `${$scope.url}/rad/${elementName}?openapi.json`;
      } else if (element.spec.swagger != undefined) {
        url = `${$scope.url}/rad/${elementName}?swagger.json`;
      }
      fetch(url, options)
        .then((res) => {
          console.log(res["statusText"])
          if(res["statusText"]=='OK'){
            return res.blob().then(function (response) {
          var a = document.createElement("a");
          document.body.appendChild(a);
          a.style = "display: none";
          a.href = window.URL.createObjectURL(response);
          a.download = `${elementName.split(':').pop()}.json`;
          a.click();
            });
          }else if(res["statusText"]!='OK'){
          var node = document.createElement("div");
          node.innerHTML =
              '<div class="title-wrapper"><span class="dlt-icon-error error-icon"></span><div class="toast-title">Error</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">The server is not responding. Try again later.</div>';
          node.classList.add("dlt-toast");
            node.classList.add("error-border");
          node.style.marginLeft = "auto";
          node.style.position = "fixed";
          node.style.top = "75px";
          node.style.right = "25px";
          document.getElementsByTagName("body")[0].appendChild(node);
            if(res['statusText']=='Not Found'){
              document.getElementsByClassName("toast-message")[0].innerHTML =
              "Unable to download the JSON document. RAD not Found.";
            }else{
              document.getElementsByClassName("toast-message")[0].innerHTML =
              "Unable to download the JSON document. " + res['statusText'];
            }
          document.getElementsByClassName("dlt-toast")[0].style.display =
            "block";
          }
        })
        .catch((error) => {
          var node = document.createElement("div");
          node.innerHTML =
            '<div class="title-wrapper"><span class="dlt-icon-error error-icon"></span><div class="toast-title">Error</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">The server is not responding. Try again later.</div>';
          node.classList.add("dlt-toast");
          node.classList.add("error-border");
          node.style.marginLeft = "auto";
          node.style.position = "fixed";
          node.style.top = "75px";
          node.style.right = "25px";
          document.getElementsByTagName("body")[0].appendChild(node);
          document.getElementsByClassName("toast-message")[0].innerHTML =
            "Unable to download the JSON document. " + error;
          document.getElementsByClassName("dlt-toast")[0].style.display =
            "block";
        });
    };

    downloadYaml = function (element) {
      let elementName = $scope.apiMap.get(element.name);
      const options = {
        headers: {
          "Content-Disposition": "attachment",
          filename: `${elementName}.yaml`,
        },
      };
      var element = $scope.apis.filter((api) => api.name === elementName)[0];
      var url = "";
      if (element.spec.openapi != undefined) {
        url = `${$scope.url}/rad/${elementName}?openapi.yaml`;
      } else if (element.spec.swagger != undefined) {
        url = `${$scope.url}/rad/${elementName}?swagger.yaml`;
      }
      fetch(url, options)
        .then((res) => {
          console.log(res["statusText"])
          if(res["statusText"]=='OK'){
            return res.blob().then(function (response) {
          var a = document.createElement("a");
          document.body.appendChild(a);
          a.style = "display: none";
          a.href = window.URL.createObjectURL(response);
          a.download = `${elementName.split(':').pop()}.yaml`;
          a.click();
            });
          }else if(res["statusText"]!='OK'){
          var node = document.createElement("div");
          node.innerHTML =
              '<div class="title-wrapper"><span class="dlt-icon-error error-icon"></span><div class="toast-title">Error</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">The server is not responding. Try again later.</div>';
          node.classList.add("dlt-toast");
            node.classList.add("error-border");
          node.style.marginLeft = "auto";
          node.style.position = "fixed";
          node.style.top = "75px";
          node.style.right = "25px";
          document.getElementsByTagName("body")[0].appendChild(node);
            if(res['statusText']=='Not Found'){
              document.getElementsByClassName("toast-message")[0].innerHTML =
              "Unable to download the YAML document. RAD not Found.";
            }else{
              document.getElementsByClassName("toast-message")[0].innerHTML =
              "Unable to download the YAML document. " + res['statusText'];
            }
          document.getElementsByClassName("dlt-toast")[0].style.display =
            "block";
          }
        })
        .catch((error) => {
          var node = document.createElement("div");
          node.innerHTML =
            '<div class="title-wrapper"><span class="dlt-icon-error error-icon"></span><div class="toast-title">Error</div><i class="dlt-icon-close close-toast"></i></div><div class="toast-message">The server is not responding. Try again later.</div>';
          node.classList.add("dlt-toast");
          node.classList.add("error-border");
          node.style.marginLeft = "auto";
          node.style.position = "fixed";
          node.style.top = "75px";
          node.style.right = "25px";
          document.getElementsByTagName("body")[0].appendChild(node);
          document.getElementsByClassName("toast-message")[0].innerHTML =
            "Unable to download the YAML document. " + error;
          document.getElementsByClassName("dlt-toast")[0].style.display =
            "block";
        });
    };

    testService = function (element) {
      window.open(
        `${$scope.url}/WmRoot/service-test.dsp?service=${
          element.name.split(":")[1]
        }&interface=${
          element.name.split(":")[0]
        }&doc=edit&webMethods-wM-AdminUI=true`,
        "_blank"
      );
    };

    $(".serv-des").click(function () {
      if ($(this).css("transform") == "none") {
        $(this).css("transform", "rotate(180deg)");
        $(this).css("transition", "transform 0.25s");
      } else {
        $(this).css("transform", "");
      }
    });

    $scope.removeNewLines = function (input) {
      return input.trim().replaceAll(/(\r\n|\n|\r)/gm, "");
    };

    $scope.changeResponseFormat = function () {
      $scope.responseFormat = this.responseFormat;
    };

    $scope.changeDefaultResponseFormat = function () {
      $scope.defaultResponseFormat = this.defaultResponseFormat;
    };

    $scope.changeRequestFormat = function () {
      $scope.requestFormat = this.requestFormat;
    };

    $scope.$watch("responseFormat", function () {});

    Array.prototype.forEach.call($scope.apis, function (value, i) {
      const ui = SwaggerUIBundle({
        spec: value["spec"],
        dom_id: `#swagger-ui-${i}`,
        deepLinking: true,
        presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],
        plugins: [SwaggerUIBundle.plugins.DownloadUrl],
        layout: "StandaloneLayout",
      });

      window.ui = ui;
    });
    $scope.changeDisplay("auth-wrapper", "none");
    var schemes = document.getElementsByClassName("scheme-container");
    for(k=0;k<schemes.length;k++){
      $scope.schemes.set(document.getElementsByClassName("scheme-container")[k].parentNode.parentNode.parentNode.parentNode.parentElement.getAttribute("id"),document.getElementsByClassName("scheme-container")[k])
    }
    $scope.removeElements("scheme-container");
  }

  window.onload  = () => {
    var apiList = document.getElementById("apis").getElementsByClassName("dlt-accordion-item");
    for(j=0;j<apiList.length;j++){
      var apiElement = apiList[j].getElementsByTagName("button")[0];
      $scope.apiMap.set(`api${j}`,apiElement.getAttribute("title"))
      document.getElementById(`api${j}`).innerHTML = document.getElementById(`api${j}`).innerHTML.replace(apiElement.getAttribute("title"),apiElement.getAttribute("title").split(':').pop());
      document.getElementById(`api${j}`).setAttribute("title",apiElement.getAttribute("title").split(':').pop());
    }
    var servicesList = document.getElementById("services").getElementsByClassName("dlt-accordion-item");
    for(j=0;j<servicesList.length;j++){
      var service = servicesList[j].getElementsByTagName("button")[0];
      $scope.serviceMap.set(`service${j}`,service.getAttribute("title"))
      document.getElementById(`service${j}`).innerHTML = document.getElementById(`service${j}`).innerHTML.replace(service.getAttribute("title"),service.getAttribute("title").split(':').pop());
      document.getElementById(`service${j}`).setAttribute("title",service.getAttribute("title").split(':').pop());
    }
    for(i=0;i<servicesList.length;i++){
      var inputList = document.getElementById(`servicebody${i}`).getElementsByClassName("input-row");
      for(j=0;j<inputList.length;j++){
        var input = document.getElementById(`${i}${j}iname`);
        $scope.ioMap.set(`${i}${j}iname`,input.getAttribute("title"));
        document.getElementById(`${i}${j}iname`).innerHTML = document.getElementById(`${i}${j}iname`).innerHTML.replace(/([a-zA-Z0-9_]+[\.])+/,'').replace(/([a-zA-Z0-9_]+)+:/,'');
        document.getElementById(`${i}${j}iname`).setAttribute("title",input.getAttribute("title").replace(/([a-zA-Z0-9_]+[\.])+/,'').replace(/([a-zA-Z0-9_]+)+:/,''));
      }
      var outputList = document.getElementById(`servicebody${i}`).getElementsByClassName("output-row");
      for(j=0;j<outputList.length;j++){
        var output = document.getElementById(`${i}${j}oname`);
        $scope.ioMap.set(`${i}${j}oname`,output.getAttribute("title"))
        document.getElementById(`${i}${j}oname`).innerHTML = document.getElementById(`${i}${j}oname`).innerHTML.replace(/([a-zA-Z0-9_]+[\.])+/,'').replace(/([a-zA-Z0-9_]+)+:/,'');
        document.getElementById(`${i}${j}oname`).setAttribute("title",output.getAttribute("title").replace(/([a-zA-Z0-9_]+[\.])+/,'').replace(/([a-zA-Z0-9_]+)+:/,''));
      }
    }
    fetch(`${location.origin}/invoke/pub.date/getCurrentDate`)
  .then((response) => {
    if (response["status"] === 200) {
      $scope.isAuthenticated = true;
      isDownload = true;
    } else if (response["status"] === 401) {
      $scope.isAuthenticated = false;
      isDownload = true;
    } else {
      $scope.isAuthenticated = false;
      isDownload = false;
    }
  })
  .catch((error) => {
    $scope.isAuthenticated = false;
    isDownload = false;
  }).finally(()=>{
    $scope.initWindow();
  });
  };

});