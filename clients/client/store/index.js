import Vue from 'vue'
import Vuex from 'vuex'
import pkg from 'package'
import * as actions from './actions'
import * as getters from './getters'

import app from './modules/app'
import menu from './modules/menu'
import dashboard from './modules/dashboard/dashboard'
import tracksystem from './modules/tracksystem/tracksystem'
import tools from './modules/tools/tools'

Vue.use(Vuex)

const store = new Vuex.Store({
  strict: true,  // process.env.NODE_ENV !== 'production',
  actions,
  getters,
  modules: {
    app,
    menu,
    dashboard,
    tracksystem,
    tools
  },
  state: {
    pkg
  },
  mutations: {
  }
})

export default store
