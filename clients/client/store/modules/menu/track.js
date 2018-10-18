import lazyLoading from './lazyLoading'

export default {
  meta: {
    label: 'Track System',
    icon: 'fa-list-ul',
    expanded: false
  },

  children: [
    {
      name: 'Deployment',
      path: '/track/deployment',
      component: lazyLoading('track/deployment/index')
    },
    {
      name: 'Appliances',
      path: '/track/appliances',
      component: lazyLoading('track/appliances/index')
    }
  ]
}
