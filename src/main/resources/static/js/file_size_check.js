
// 파일 용량 javascript 요청 처리
$(document).ready(function() {

   let resultFileSizes = 0; // 총 파일 용량
   let arrayFileList = new Array(); // 파일 한 개씩 담는 배열

   $('#multipartFileList').change(function() {
       console.log("type = file 값이 변경되었다");
       let allFilesSizes = 0;

       // 배열로 바꾸기
       arrayFileList = Array.from(this.files);

        // 모두 더한다
       arrayFileList.forEach((file) => {
           allFilesSizes += file.size;
       });
//       console.log("allFilesSizes 총량 : " + allFilesSizes);

       resultFileSizes = allFilesSizes;
//       event.stopImmediatePropagation();
//       return false;
   });

   // 파일 용량 체크
   $("form").on('submit', function() {

      // 이벤트 알림
//      alert("submit click");
      const limitedTotalFileSize = 1024 * 1024 * 10; // 10MB // 총 파일 용량 제한
      const limitedFileSize = 1024 * 1024 * 1; // 1MB 파일 한 개당

      // 파일 한 개당 1MB를 넘으면 return false
      for(let i = 0; i < arrayFileList.length; i++) {

          // 파일 한 개씩 검사하기
          let oneFileSize = arrayFileList[i].size;
          if(oneFileSize >= limitedFileSize) {
              alert("파일 한 개 저장 용량(1MB)을 초과하였습니다.");
              console.log("oneFileSize : ", arrayFileList[i].size);
              // file value를 초기화하기
              $("#multipartFileList").val("");
              return false;
          }
      }

      // 10MB보다 높으면 return false
      if(resultFileSizes >= limitedTotalFileSize) {
          alert("파일 저장 용량(10MB)을 초과하였습니다.");
          // file value를 초기화하기
          $("#multipartFileList").val("");
          return false;
      }
      // 조건을 통과하면 post 진행
   });
});

// 파일 데이터를 가져오지 못함
//$(document).ready(function() {
//
//   // 파일 용량 체크
//   $("form").on('submit', function() {
//      // 이벤트 알림
//      alert("submit click");
//
//      // 파일 저장 용량 불러오기 시작
//
//      let allFilesSizes = 0;
//
//      // 배열로 바꾸기
//      let arrayFileList = Array.from($('#multipartFileList').files);
//      console.log("arrayFileList : " + arrayFileList);
//
//      // 모두 더한다
//      arrayFileList.forEach((file) => {
//         allFilesSizes += file.size;
//      });
//      alert("allFilesSizes 총량 : " + allFilesSizes);
//
//      // 파일 저장 용량 불러오기 끝
//
//      const limitedFileSize = 1024 * 1024 * 10;
//
//      // 10MB보다 높으면 return false
//      if(allFilesSizes >= limitedFileSize) {
//           alert("파일 저장 용량(10MB)을 초과하였습니다.");
//           // file value를 초기화하기
//           $("#multipartFileList").val("");
//           return false;
//      }
//      // 조건을 통과하면 post 진행
//   });
//});

// 함수로 표현하면 값이 왜 전달이 안 될까?
//$(document).ready(function() {
//
//    const allFileSizes = changeFileInput();
//
//    // 파일 용량 체크
//    $("form").on('submit', function() {
//
//       // 이벤트 알림
//       alert("submit click");
//       let limitedFileSize = 1024 * 1024 * 10;
//
//       alert("allFileSizes : " + allFileSizes);
//
//       // 10MB보다 높으면 return false
//       if(allFileSizes >= limitedFileSize) {
//            alert("파일 저장 용량을 초과하였습니다.");
//            return false;
//       }
//       // 조건을 통과하면 post 진행
//       return true;
//    });
//});
//
///** 파일 용량 체크 **/
//// 10MB 이상이면 사용 못함
//let checkFileStorageCapacity = function(allFilesSizes) {
//
//    // submit 클릭하면 - post 되면
//    $("form").on('submit', function(allFilesSizes) {
//
//       // 이벤트 알림
//       alert("submit click");
//       let limitedFileSize = 1024 * 1024 * 10;
//
//       alert("allFilesSizesNum : " + allFilesSizesNum);
//
//       // 10MB보다 높으면 return false
//       if(allFilesSizesNum >= limitedFileSize) {
//            return false;
//       }
//       // 조건을 통과하면 post 진행
//       return true;
//    });
//}
//
///** 파일 input에  변화가 있을 때 값 가져오기 **/
//let changeFileInput = function() {
//
//   let filesSizes = 0;
//   $('#multipartFileList').change(function() {
//        alert("type = file 값이 변경되었다");
//        let allFilesSizes = 0;
//
//        // 배열로 바꾸기
//        let arrayFileList = Array.from(this.files);
////        console.log("arrayFileList : " + arrayFileList);
//
//        // 모두 더한다
//        arrayFileList.forEach((file) => {
//            allFilesSizes += file.size;
//        });
//        alert("allFilesSizes 총량 : " + allFilesSizes);
//
//        filesSizes = allFilesSizes;
//   });
//   return filesSizes;
//}