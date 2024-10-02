$(document).ready(function() {
	
	$('.dashboardWrapperLeftPanel').removeClass("dashboardWrapperLeftPanelActive");
	$('.dashboardWrapperRightPanel').removeClass("dashboardWrapperRightPanelActive");
	
//    $('body').addClass('dashboardPreload')

//    setTimeout(function() {
//        $('.dashboardNewBooking').click()
//    }, 500);

    $('.dashboardBookingSelectDriver').click(function() {

    });

    $('.dashboardBookingLuggagePlus').click(function() {
        $('.dashboardBookingLuggageValue').val(parseInt($('.dashboardBookingLuggageValue').val()) + 1)
    });

    $('.dashboardBookingLuggageMinus').click(function() {

        if (parseInt($('.dashboardBookingLuggageValue').val()) > 0) {
            $('.dashboardBookingLuggageValue').val(parseInt($('.dashboardBookingLuggageValue').val()) - 1)
        }
    });

    $('.dashboardNewBooking').click(function() {
        $('.dashboardWrapperRightPanelMapInnerCustomOverlay').fadeIn();
    });

    $('.dashboardOverlayMainClose span').click(function() {
        $('.dashboardWrapperRightPanelMapInnerCustomOverlay').fadeOut();
    });

//    if (dashboard_CheckMenuOpen()) {
//        setTimeout(function() {
//            $('.dashboardWrapperLeftPanelTitleMenuButton').click()
//            
//        }, 10)
//    }
    
    $('.dashboardWrapperLeftPanelTitleMenuButton').click(function() {
    	
    	 $('.dashboardWrapperLeftPanel').toggleClass("dashboardWrapperLeftPanelActive");
    	 $('.dashboardWrapperRightPanel').toggleClass("dashboardWrapperRightPanelActive");
    	
//        if ($(this).closest('.dashboardWrapperLeftPanel').hasClass('dashboardWrapperLeftPanelActive')) {
//        	
//        	//menu is open
//        	
//            $(this).closest('.dashboardWrapperLeftPanel').removeClass('dashboardWrapperLeftPanelActive')
//            $('.dashboardWrapperRightPanel').removeClass('dashboardWrapperRightPanelActive');
//            localStorage.setItem('isMenuOpen', '0');
//            
//            jQuery("ul.dashboardWrapperLeftPanelMenuList li a").css("font-size", "13px");
//            
//        } else {
//        	
//        	//menu is closed
//        	
//            $(this).closest('.dashboardWrapperLeftPanel').addClass('dashboardWrapperLeftPanelActive')
//            $('.dashboardWrapperRightPanel').addClass('dashboardWrapperRightPanelActive');
//            localStorage.setItem('isMenuOpen', '1');
//            
//            jQuery("ul.dashboardWrapperLeftPanelMenuList li a").css("font-size", "11px");
//        }
    });

    $('.dashboardWrapperLeftPanelMenuListSubMenu').click(function() {
        $(this).find('.dashboardWrapperLeftPanelMenuListSubMenuInner').slideToggle();
    });

    if (typeof(Storage) !== "undefined") {

    } else {

    }
});

function dashboard_CheckMenuOpen() {
	
    if (localStorage.getItem('isMenuOpen') == 1) {
        return true;
    } else {
        return false;
    }
}

window.onload = function() {
    $('body').removeClass('dashboardPreload');
}