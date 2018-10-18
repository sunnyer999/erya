import lazyLoading from './lazyLoading'

export default {
  meta: {
    label: 'Tools',
    icon: 'fa-archive',
    expanded: false
  },

  children: [
    {
      name: 'Quick Chat',
      path: '/tools/quick-chat',
      component: lazyLoading('tools/quick-chat')
    }, {
      name: 'Robot Booster',
      path: '/tools/robot-booster',
      component: lazyLoading('tools/robot-booster')
    }, {
      name: 'Appliance Checker',
      path: '/tools/appliance-checker',
      component: lazyLoading('tools/verify/index')
    }
  ]
}
