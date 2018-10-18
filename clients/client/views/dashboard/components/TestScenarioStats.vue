<template>
  <div id="test-scenario">
    <nav class="level">
      <div class="level-left">
        <div class="level-item animated flipInX">
          <p class="subtitle">
            <span class="tag is-warning">Avg Elapse > 3hr</span>
            <span class="tag is-danger">Avg PassRate < 95%</span>
          </p>
        </div>
      </div>
      <div class="level-right">
        <div class="level-item">
          <p class="control has-icon">
            <input class="input" @input="filterTests" type="text" placeholder="Scenario or owner">
            <span class="icon is-small">
              <i class="fa fa-search"></i>
            </span>
          </p>
        </div>
      </div>
    </nav>
    <div class="table-responsive">
      <table class="table is-narrow is-bordered ">
        <thead>
          <tr>
            <th><i class="fa fa-list-ul" aria-hidden="true" type="is-link"></i></th>
            <th @click="sortTestScenarios('jobName')">Scenario
              <a v-if="testScenarioSortKey==='jobName'">
                <i :class="{ 'el-icon-caret-bottom': ascTestScenarios, 'el-icon-caret-top': !ascTestScenarios}"></i>
              </a>
              <a v-else>
                <i class="el-icon-d-caret"></i>
              </a>
            </th>
            <th @click="sortTestScenarios('owner')">Owner
              <a v-if="testScenarioSortKey==='owner'">
                <i :class="{ 'el-icon-caret-bottom': ascTestScenarios, 'el-icon-caret-top': !ascTestScenarios }"></i>
              </a>
              <a v-else>
                <i class="el-icon-d-caret"></i>
              </a>
            </th>
            <th @click="sortTestScenarios('maxElapse')">Max Elapse
              <a v-if="testScenarioSortKey==='maxElapse'">
                <i :class="{ 'el-icon-caret-bottom': ascTestScenarios, 'el-icon-caret-top': !ascTestScenarios}"></i>
              </a>
              <a v-else>
                <i class="el-icon-d-caret"></i>
              </a>
            </th>
            <th @click="sortTestScenarios('avgElapse')">Avg Elapse
              <a v-if="testScenarioSortKey==='avgElapse'">
                <i :class="{ 'el-icon-caret-bottom': ascTestScenarios, 'el-icon-caret-top': !ascTestScenarios }"></i>
              </a>
              <a v-else>
                <i class="el-icon-d-caret"></i>
              </a>
            </th>
            <th>Elapse Trend</th>
            <th @click="sortTestScenarios('minPassRate')">Min PassRate
              <a v-if="testScenarioSortKey==='minPassRate'">
                <i :class="{ 'el-icon-caret-bottom': ascTestScenarios, 'el-icon-caret-top': !ascTestScenarios}"></i>
              </a>
              <a v-else>
                <i class="el-icon-d-caret"></i>
              </a>
            </th>
            <th @click="sortTestScenarios('maxPassRate')">Max PassRate
              <a v-if="testScenarioSortKey==='maxPassRate'">
                <i :class="{ 'el-icon-caret-bottom': ascTestScenarios , 'el-icon-caret-top': !ascTestScenarios}"></i>
              </a>
              <a v-else>
                <i class="el-icon-d-caret"></i>
              </a>
            </th>
            <th @click="sortTestScenarios('avgPassRate')">Avg PassRate
              <a v-if="testScenarioSortKey==='avgPassRate'">
                <i :class="{ 'el-icon-caret-bottom': ascTestScenarios, 'el-icon-caret-top': !ascTestScenarios}"></i>
              </a>
              <a v-else>
                <i class="el-icon-d-caret"></i>
              </a>
            </th>
            <th>PassRate Trend</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(test, index) in filteredTestOption" :key="test.jobName" class="animated flipInX">
            <td>
              <el-tag>{{index + 1}}</el-tag>
            </td>
            <td>
              <tooltip :label="test.jobName" size="large" placement="top">
                <button class="button is-primary">
                        <span>{{test.jobName | truncate}}</span>
                      </button>
              </tooltip>
            </td>
            <td><i class="fa fa-address-book-o">&nbsp;{{test.owner}}</i></td>
            <td>{{test.maxElapse | duration('h:mm:ss')}}</td>
            <td>{{test.avgElapse | duration('h:mm:ss')}}</td>
            <td>
              <highcharts :options="test.elapseOption" ref="highcharts"></highcharts>
            </td>
            <td>{{test.minPassRate + '%'}}</td>
            <td>{{test.maxPassRate + '%'}}</td>
            <td>{{test.avgPassRate + '%'}}</td>
            <td>
              <highcharts :options="test.passRateOption" ref="highcharts"></highcharts>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import 'moment-duration-format'
import _ from 'lodash'
import Tooltip from 'vue-bulma-tooltip'

export default {
  props: {
    title: {
      type: String,
      required: true
    },
    pipelineName: {
      type: String,
      required: true
    }
  },

  data () {
    return {
      ascTestScenarios: true,
      testScenarioSortKey: '',
      testsFilter: ''
    }
  },

  watch: {
    pipelineName (value) {
      this.$store.dispatch('getTestScenarios', {'testPipeline': this.pipelineName})
    }
  },

  components: {
    Tooltip
  },

  computed: {
    ...mapGetters({
      testOption: 'testOption'
    }),

    filteredTestOption () {
      if (!this.testsFilter) {
        return this.testOption
      }
      return _.filter(this.testOption, test => {
        return _.find(_.map(_.values(test), _.toString), v => _.toLower(v).includes(_.toLower(this.testsFilter)))
      })
    }
  },

  methods: {
    filterTests: _.debounce(function (e) {
      this.testsFilter = e.target.value
    }, 350),

    sortTestScenarios (sortKey, reversed) {
      let dir = this.ascTestScenarios ? 'asc' : 'desc'
      this.$store.commit('SORT_TEST_SCENARIOS', {'sortKey': sortKey, 'direction': dir})
      this.ascTestScenarios = !this.ascTestScenarios
      this.testScenarioSortKey = sortKey
    }
  },

  created () {
    this.$store.dispatch('getTestScenarios', { 'testPipeline': this.pipelineName })
  }
}
</script>

<style lang="scss" scoped>
  #test-scenario {
    margin-top: 20px
  }
.table-responsive {
  display: block;
  width: 100%;
  min-height: .01%;
  overflow-x: auto;
}
</style>
