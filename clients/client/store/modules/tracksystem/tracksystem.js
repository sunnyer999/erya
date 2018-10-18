import axios from 'axios'
import * as APIs from '../../api/api.js'
import * as types from '../../mutation-types'
import format from 'string-template'
import _ from 'lodash'

const state = {
  failedDeploymentStats: []
}

const getters = {
  failedDeploymentStats: state => state.failedDeploymentStats
}

const actions = {
  getDeployApplianceStats ({commit}, payload) {
    let url = format(APIs.GET_FAIL_DEPLOYMENT, [payload.deployPipeline, payload.deployName])
    axios.get(url).then(
      (resp) => {
        commit(types.RECEVIE_FAILED_DEPLOYMENT, {testResult: resp.data})
      }, (err) => {
      console.log(err)
    })
  }
}

const mutations = {
  [types.RECEVIE_FAILED_DEPLOYMENT] (state, {testResult}) {
    var failedDeployment = _.reduce(testResult, (result, c) => {
      result.push({'triggerId': c.deployments[0].triggerId, 'appliances': c.deployments[0].failedDeployments})
      return result
    }, [])
    state.failedDeploymentStats = failedDeployment
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
