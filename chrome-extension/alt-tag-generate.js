//현재 화면에 보이는 태그인지 확인하는 함수
function isElementInViewport(el) {
  var rect = el.getBoundingClientRect();
  return (
    rect.top >= 0 &&
    rect.left >= 0 &&
    rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
    rect.right <= (window.innerWidth || document.documentElement.clientWidth)
  );
}

{
	API_URL = "http://127.0.0.1:5000/"
	// 모든 img 태그 선택
	const imgTags = document.querySelectorAll("img");
	console.log(imgTags);
	// img 태그를 반복하면서 alt 속성이 없는 경우 수정
	imgTags.forEach((img) => {
		isVisible = isElementInViewport(img);
		const altText = img.getAttribute("alt");
		if (isVisible) {
			fetch(API_URL).then((response) => response.json())
			.then((data) => {
				new_alt = data['altText'];
				img.setAttribute("alt", new_alt);
				console.log(new_alt, "대체 텍스트 생성!");
			})
		}
	});
}
