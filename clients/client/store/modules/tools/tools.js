import axios from 'axios'
import * as APIs from '../../api/api.js'
import * as types from '../../mutation-types'
import format from 'string-template'
// import _ from 'lodash'

const state = {
  verifyStatsByPage: [],
  verifyTestbeds: []
}

const getters = {
  verifyStatsByPage: state => state.verifyStatsByPage,
  verifyTestbeds: state => state.verifyTestbeds,
  totalVerifyJob: state => {
    if (state.verifyStatsByPage.totalPages) {
      return state.verifyStatsByPage.totalPages
    }
    return 0
  }
}

const actions = {
  cancelCheckAppliance ({commit}, payload) {
    let url = format(APIs.CANCEL_CHECK_APPLIANCE, [payload.hostname])
    axios.get(url).then(
      (resp) => {
        payload.vm.$notify({
          message: payload.hostname + ' cancel Successfully',
          type: 'success',
          offset: 100
        })
      }, (err) => {
      console.log(err)
      payload.vm.$notify({
        message: payload.hostname + ' cancel Failed',
        type: 'warning',
        offset: 100
      })
    })
  },
  getVerifyStatsByPage ({commit}, payload) {
    let url = format(APIs.GET_VERIFY_LIST, [payload.page])
    axios.get(url).then(
      (resp) => {
        commit(types.RECEVIE_VERIFY_LIST, {testResult: resp.data})
      }, (err) => {
      console.log(err)
    })
  },
  getTestbedsInfo ({commit}, payload) {
    let url = format(APIs.GET_TESTBED_INFO, [payload.hostname])
    axios.get(url).then(
      (resp) => {
        commit(types.RECEVIE_TESTBEDS_INFO, {testResult: resp.data})
      }, (err) => {
      console.log(err)
    })
  },
  postVerifyJob ({commit}, payload) {
    let url = format(APIs.POST_VERIFY_LIST)
    axios.post(url, {email: payload.email, hostname: payload.hostname, model: payload.model, site: payload.site, type: payload.type, username: payload.username}).then(
      (resp) => {
        payload.vm.$notify({
          message: payload.hostname + ' Submitted Successfully',
          type: 'success',
          offset: 100
        })
      }, (err) => {
      console.log(err)
      payload.vm.$notify({
        message: payload.hostname + ' Submitted Failed',
        type: 'warning',
        offset: 100
      })
    })
  }
}

const mutations = {
  [types.RECEVIE_VERIFY_LIST] (state, {testResult}) {
    state.verifyStatsByPage = testResult
  },

  [types.RECEVIE_TESTBEDS_INFO] (state, {testResult}) {
    state.verifyTestbeds = testResult
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
