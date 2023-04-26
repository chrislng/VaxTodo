/*!
* Start Bootstrap - Simple Sidebar v6.0.5 (https://startbootstrap.com/template/simple-sidebar)
* Copyright 2013-2022 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-simple-sidebar/blob/master/LICENSE)
*/
// 
// Scripts
// 

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    const wrapper = document.querySelector('#wrapper');
    
    // These four constants serve to get the transition 
    const sidebarWrapper = document.querySelector('#sidebar-wrapper');
    const sidebarWrapperStyles = window.getComputedStyle(sidebarWrapper);
    const sidebarWrapperTransitionDurationSeconds = sidebarWrapperStyles.getPropertyValue('transition-duration');
    const sidebarWrapperTransitionDurationMilliseconds = parseFloat(sidebarWrapperTransitionDurationSeconds) * 1000;
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
            /* Modify the #wrapper z-index from -1 (its default value) to 0 when #sidebarToggle is clicked, 
                to make the sidebar visible,
            and from 0 to -1 when #sidebarToggle is clicked again, 
                to place #wrapper behind the body again and thus allow interaction with the page elements.
            */
            if (wrapper.style.zIndex === '0') {
                /* So that the z-index goes back to -1 (behind the body) only after the #sidebar-wrapper margine-left 
                   transition has occured. */
                setTimeout(function() {
                    wrapper.style.zIndex = '-1';
                }, sidebarWrapperTransitionDurationMilliseconds);
            } else {
                wrapper.style.zIndex = '0';
            }
        });
    }

});
