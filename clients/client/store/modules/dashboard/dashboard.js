import axios from 'axios'
import * as APIs from '../../api/api.js'
import * as types from '../../mutation-types'
import Highcharts from 'highcharts'
import _ from 'lodash'
import format from 'string-template'
import moment from 'moment'
import 'moment-duration-format'
import { OWNER } from './scenario-owners'

const threeHoursAsMilliseconds = moment.duration(3, 'hours').asMilliseconds()
const percent95PassRate = 95

const testTrendTemplate = {
  chart: {
    backgroundColor: null,
    borderWidth: 0,
    type: 'area',
    margin: [2, 0, 2, 0],
    width: 120,
    height: 20,
    style: {
      overflow: 'visible'
    },
    skipClone: true
  },
  series: [{
    data: [],
    color: '#00bcd4',
    tooltip: {
    }
  }],
  title: { text: '' },
  credits: {
    enabled: false
  },
  xAxis: {
    labels: {
      enabled: false
    },
    title: {
      text: null
    },
    startOnTick: false,
    endOnTick: false,
    tickPositions: []
  },
  yAxis: {
    endOnTick: false,
    startOnTick: false,
    labels: {
      enabled: false
    },
    title: {
      text: null
    },
    tickPositions: [0]
  },
  legend: {
    enabled: false
  },
  tooltip: {
    backgroundColor: null,
    borderWidth: 0,
    shadow: false,
    useHTML: true,
    hideDelay: 0,
    shared: true,
    padding: 0,
    positioner: function (w, h, point) {
      return { x: point.plotX - w / 2, y: point.plotY - h }
    }
  },
  plotOptions: {
    series: {
      animation: false,
      lineWidth: 1,
      shadow: false,
      states: {
        hover: {
          lineWidth: 1
        }
      },
      marker: {
        radius: 1,
        states: {
          hover: {
            radius: 2
          }
        }
      },
      fillOpacity: 0.25
    },
    column: {
      negativeColor: '#910000',
      borderColor: 'silver'
    }
  }
}

const state = {
  login: {
    loggedin: false,
    username: '',
    password: ''
  },
  deployStats: {
    chart: {
      type: 'column'
    },
    credits: {
      enabled: false
    },
    title: {
      text: ''
    },
    xAxis: {
      categories: []
    },
    yAxis: {
      labels: {
        format: '{value} %'
      },
      min: 0,
      title: {
        text: 'ISO Installation Success Rate'
      }
    },
    plotOptions: {
      areaspline: {
        fillOpacity: 0.2,
        color: '#00bcd4'
      },
      column: {
        stacking: 'percent',
        dataLabels: {
          enabled: true,
          color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || '{series.color}'
        }
      }
    },
    series: [{
      name: 'Failed',
      data: [],
      color: '#ffeb3b'
    }, {
      name: 'Success',
      data: [],
      color: '#b2dfdb'
    }, {
      type: 'areaspline',
      name: 'Success Rate',
      data: [],
      tooltip: {
        valueSuffix: ' %'
      },
      marker: {
        lineWidth: 2,
        lineColor: Highcharts.getOptions().colors[3],
        fillColor: 'white'
      }
    }]
  },

  durationStats: {
    credits: {
      enabled: false
    },
    chart: {
      type: 'bar'
    },
    title: {
      text: 'Pipeline Duration'
    },
    tooltip: {
      formatter: function () {
        return this.series.name + ': ' + moment.duration(this.y).format('h:mm:ss', { trim: false })
      }
    },
    xAxis: {
      categories: []
    },
    yAxis: {
      min: 0,
      title: {
        text: 'Elapse Time (hh:mm:ss)'
      },
      labels: {
        formatter: function () {
          return moment.duration(this.value).format('h:mm:ss', { trim: false })
        }
      }
    },
    legend: {
      reversed: true
    },
    plotOptions: {
      series: {
        stacking: 'normal',
        point: {
          events: {
            click: function () {
              window.open(this.options.url)
            }
          }
        }
      }
    },
    series: [{
      name: 'Report Time',
      URLs: [],
      data: [],
      color: '#b2ebf2'
    }, {
      name: 'Test Time',
      data: [],
      color: '#00bcd4'
    }, {
      name: 'Deploy Time',
      data: [],
      color: '#b2dfdb'
    }, {
      name: 'Build Time',
      data: [],
      color: '#64b5f6'
    }]
  },

  testStats: [],

  latestTestScenarioStats: {},

  recentPipelineTrends: {},

  deployStatus: [],

  deployStatusByPage: []
}

const getters = {
  loguser: state => state.login.username,
  deployOption: state => state.deployStats,
  durationOption: state => state.durationStats,
  testOption: state => state.testStats,
  latestTestScenarioStats: state => state.latestTestScenarioStats,
  recentPipelineTrends: state => state.recentPipelineTrends,
  deployStatusByPage: state => state.deployStatusByPage,
  total: state => {
    if (state.deployStatus.deployStatus) {
      return state.deployStatus.deployStatus.length
    }
    return 0
  }
}

const actions = {
  getDurationStats ({ commit }, payload) {
    var url = format(APIs.GET_DURATION, [payload.durationPipeline])
    axios.get(url).then(
      (resp) => {
        commit(types.RECEIVE_DURATION_DATA, { durationResult: resp.data })
      }, (err) => {
      console.log(err)
    })
  },

  getDeployStats ({ commit }, payload) {
    var url = format(APIs.GET_CI_DEPLOYMENTS_TYPE, [payload.timeScopeType, payload.deployPipeline, payload.deployName, payload.dateBegin, payload.dateEnd])
    axios.get(url).then(
      (resp) => {
        commit(types.RECEIVE_DEPLOY_DATA, { deployResult: resp.data, deployName: payload.deployName })
      }, (err) => {
      console.log(err)
    })
  },
  getusername ({ commit }, payload) {
   // console.log(payload.username)
    commit(types.RECEIVE_USER_INFORMATION, { username: payload.username, password: payload.password })
  },
  getTestScenarios ({ commit }, payload) {
    commit(types.CLEAN_TEST_SCENARIOS)
    var url = format(APIs.GET_TEST_SCENARIO, payload.testPipeline)
    axios.get(url).then(
      (resp) => {
        commit(types.RECEIVE_TEST_DATA, { testResult: resp.data })
      }, (err) => {
      console.log(err)
    })
  },

  getTestScenariosLatest ({commit}, payload) {
    let url = format(APIs.GET_LATEST_TEST_SCENARIO, {pipeline: payload.pipeline, triggerId: payload.triggerId || 0})
    axios.get(url).then(
      (resp) => {
        commit(types.RECEVIE_LATEST_TEST_SCENARIOS, {pipeline: payload.pipeline, latestScenarioStats: resp.data})
      }, (err) => {
      console.log(err)
    })
  },

  getPipelineRecentTrends ({ commit }, payload) {
    let url = format(APIs.GET_LATEST_TEST_SCENARIO, { pipeline: payload.pipeline, triggerId: 0 - payload.builds })
    axios.get(url).then(
      (resp) => {
        commit(types.RECEVIE_RECENT_PIPELINE_TRENDS, { pipeline: payload.pipeline, trends: resp.data })
      }, (err) => {
      console.log(err)
    })
  },

  getLatestDeployStatus ({ commit }, payload) {
    let url = format(APIs.GET_LATEST_DEPLOY_STATUS, {deployJobName: payload.deployJobName})
    axios.get(url).then(
      (resp) => {
        commit(types.RECEVIE_LATEST_DEPLOY_STATUS, { deployStatus: resp.data })
      }, (err) => {
      console.log(err)
    })
  },

  getLatestDeployStatusByPage ({ commit }, payload) {
    commit(types.GET_LATEST_DEPLOY_STATUS_BY_PAGE, {page: payload.page})
  }
}

const mutations = {
  [types.RECEIVE_DEPLOY_DATA] (state, {deployResult, deployName}) {
    state.deployStats.xAxis.categories = _.map(deployResult, result => '#' + result.category)
    state.deployStats.series[0].data = _.map(deployResult, result => result.failedDeploymentCount)
    state.deployStats.series[1].data = _.map(deployResult, result => result.totalDeploymentCount - result.failedDeploymentCount)
    state.deployStats.series[2].data = _.map(deployResult, result => Math.round(result.successRate * 100))
    state.deployStats.title.text = deployName
  },

  [types.RECEIVE_USER_INFORMATION] (state, {username, password}) {
    console.log(username)
    console.log(password)
    state.login.username = username
    state.login.password = password
  },

  [types.RECEIVE_DURATION_DATA] (state, {durationResult}) {
    var maxDurationArr = _.reduce(durationResult.content, (result, c) => {
      var maxDeployDuration = _.maxBy(c.deploymentJobs, 'duration') || 0 // Calculate the max deploy duration
      var maxTestDuration = _.maxBy(c.testJobs, 'duration') || 0 // Calculate the max test duration
      var maxReportDuration = _.maxBy(c.reportJobs, 'duration') || 0
      var maxBuildDuraion = _.maxBy(c.buildJobs, 'duration') || 0
      result[0].push({'y': maxReportDuration.duration, 'url': maxReportDuration.uri})
      result[1].push({'y': maxTestDuration.duration, 'url': maxTestDuration.uri})
      result[2].push({'y': maxDeployDuration.duration, 'url': maxDeployDuration.uri})
      result[3].push({'y': maxBuildDuraion.duration, 'url': maxBuildDuraion.uri})
      result[4].push('#' + c.triggerId)
      return result
    }, [[], [], [], [], []])
    state.durationStats.series[0].data = maxDurationArr[0]
    state.durationStats.series[1].data = maxDurationArr[1]
    state.durationStats.series[2].data = maxDurationArr[2]
    state.durationStats.series[3].data = maxDurationArr[3]
    state.durationStats.xAxis.categories = maxDurationArr[4]
  },

  [types.RECEIVE_TEST_DATA] (state, {testResult}) {
    _.map(testResult, (t) => {
      // Add owner' name
      var jobName = t.jobName
      t.owner = OWNER[jobName]

      // Count histories data
      var dataSet = _.reduce(t.histories, (result, h) => {
        result[0].unshift(h.elapse)
        result[1].unshift(h.passRate)
        return result
      }, [[], []])

      // Deep Copy Elapse Trend
      testTrendTemplate.series[0].data = dataSet[0]
      if (t.avgElapse > threeHoursAsMilliseconds) {
        testTrendTemplate.series[0].color = '#ffeb38'
      } else {
        testTrendTemplate.series[0].color = '#00bcd4'
      }
      var elapseOption = JSON.parse(JSON.stringify(testTrendTemplate))
      elapseOption.series[0].tooltip.pointFormatter = function () { return moment.duration(this.y).format('h:mm:ss', { trim: false }) }
      t['elapseOption'] = elapseOption
      // Deep Copy PassRateOption
      testTrendTemplate.series[0].data = dataSet[1]
      if (t.avgPassRate < percent95PassRate) {
        testTrendTemplate.series[0].color = '#ff6f00'
      } else {
        testTrendTemplate.series[0].color = '#00bcd4'
      }
      var passRateOption = JSON.parse(JSON.stringify(testTrendTemplate))
      passRateOption.series[0].tooltip.pointFormatter = function () { return this.y + '%' }
      elapseOption.xAxis.categories = passRateOption.xAxis.categories = _.sortBy(_.map(t.histories, h => '#' + h.triggerId))
      t['passRateOption'] = passRateOption
      // convert data format
      t.avgPassRate = _.floor(t.avgPassRate, 2)
    })
    state.testStats = testResult
  },

  [types.SORT_TEST_SCENARIOS] (state, {sortKey, direction}) {
    state.testStats = _.orderBy(state.testStats, sortKey, direction)
  },

  [types.CLEAN_TEST_SCENARIOS] (state) {
    state.testStats = []
  },

  [types.RECEVIE_LATEST_TEST_SCENARIOS] (state, {pipeline, latestScenarioStats}) {
    _.map(latestScenarioStats.scenarios, s => { s.owner = OWNER[s.name] })
    state.latestTestScenarioStats = []
    state.latestTestScenarioStats = _.assignIn({[pipeline]: latestScenarioStats}, state.latestTestScenarioStats)
  },

  [types.RECEVIE_RECENT_PIPELINE_TRENDS] (state, {pipeline, trends}) {
    state.recentPipelineTrends = _.assignIn({[pipeline]: trends}, state.recentPipelineTrends)
  },

  [types.RECEVIE_LATEST_DEPLOY_STATUS] (state, {deployStatus}) {
    var size = 10
    if (deployStatus) {
      state.deployStatus = deployStatus[0]
      state.deployStatusByPage = deployStatus[0].deployStatus.slice(0, size)
    }
  },

  [types.GET_LATEST_DEPLOY_STATUS_BY_PAGE] (state, {page}) {
    var size = 10
    let start = (page - 1) * size
    let end = Math.min(page * size, state.deployStatus.deployStatus.length)
    state.deployStatusByPage = state.deployStatus.deployStatus.slice(start, end)
  }

}

export default {
  state,
  getters,
  actions,
  mutations
}
