<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<script src="/resources/static/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript">

	window.onload = function(){

		CKEDITOR.replace('editor1',{
			enterMode:'2',
			shiftEnterMode:'3'
			});

		CKEDITOR.replace('editor2',{
			enterMode:'2',
			shiftEnterMode:'3'
			});

	}

	var getData = function(){

		var data = CKEDITOR.instances.editor1.getData();
		console.log(data);
		CKEDITOR.instances.editor2.setData(data);
	}

</script>
</head>
<body>

		 
		 <button onclick="getData()">보기</button>
         <textarea name="editor1" id="editor1" rows="10" cols="80">
             
        </textarea>
        
         <textarea name="editor2" id="editor2" rows="10" cols="80">
             
        </textarea>         
    

</body>
</html>