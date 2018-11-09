// JavaScript Document
//banner轮播
var swiper = new Swiper('#slider_1',{
	touchMoveStopPropagation:true,//阻止冒泡
	autoplayDisableOnInteraction:false,
	setWrapperSize :true,
	updateFormElements:true,
	pagination: '#slider_1 .swiper-pagination',
	slidesPerView:1,			
	loop: true,
	autoplay:3000,			
	autoHeight: false,
	preloadImages:true,
	updateOnImagesReady : true,
	lazyLoading : true,
	autoResize:true
});