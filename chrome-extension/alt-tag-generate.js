//현재 화면에 보이는 태그인지 확인하는 함수
function isElementInViewport(el) {
  var rect = el.getBoundingClientRect();
  var viewportWidth = window.innerWidth || document.documentElement.clientWidth;
  var viewportHeight = window.innerHeight || document.documentElement.clientHeight;
  var isPartial = (
    rect.bottom > 0 &&
    rect.top < viewportHeight
  );
  return isPartial;
}


function isURL(src) {
    return src.startsWith("http://") || src.startsWith("https://");
}

function isBase64(src) {
    return src.startsWith("data:") && src.indexOf("base64,") !== -1;
}

{
	API_URL = "http://www.waity.pe.kr:8081/image" //server Host 입력!
	// 모든 img 태그 선택
	const imgTags = document.querySelectorAll("img");
	// img 태그를 반복하면서 alt 속성이 없는 경우 수정
	imgTags.forEach((img) => {
		isVisible = isElementInViewport(img);
		const img_url = img.getAttribute("src");
		if (isVisible) {
			if(isURL(img_url))
			{
				fetch(API_URL + "/urlImage", {
				method: "POST",
				headers : {
					"Content-Type" : "application/json",
				},
				body: JSON.stringify({
					url: img_url
				})
				})
				.then((response) => response.json())
				.then((data) => {
					console.log(img)
					new_alt = data['altText'];
					img.setAttribute("alt", new_alt);
					console.log(new_alt, " : 대체 텍스트 생성!");
				})
				.catch(err =>console.log(err, "server error"))
			}
			else if (isBase64(img_url))
			{
				fetch(API_URL + "/base64Image", {
				method: "POST",
				headers : {
					"Content-Type" : "application/json",
				},
				body: JSON.stringify({
					base64String : img_url.substring(23)
				})
				})
				.then((response) => response.json())
				.then((data) => {
					console.log(img)
					new_alt = data['altText'];
					img.setAttribute("alt", new_alt);
					console.log(new_alt, " : 대체 텍스트 생성!");
				})
				.catch(err =>console.log("server error"))
			}
		}
	});
}
