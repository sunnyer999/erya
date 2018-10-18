import lazyLoading from './lazyLoading'

export default {
  meta: {
    label: 'Help center',
    icon: 'fa-question-circle-o',
    expanded: false
  },

  children: [
    {
      name: 'About Us',
      path: '/about',
      component: lazyLoading('helpcenter/About')
    }, {
      name: 'Report Issue',
      path: '/report/issue',
      component: lazyLoading('helpcenter/Issue')
    }
  ]
}
