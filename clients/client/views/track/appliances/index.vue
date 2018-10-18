<template>
  <div class="tile is-ancestor">
    <div class="tile is-vertical">
      <div class="tile">
        <div class="tile is-parent">
          <article class="tile is-child box">
            <p class="title">Export Appliance Inventory</p>
            <a href="http://10.220.132.200:8081/api/0.1/excel/appliances-info">
              <el-button type="primary" icon="document">Export Appliance Info
              </el-button>
            </a>
            <a href="http://10.220.132.200:8081/api/0.1/excel/appliances-inventory">
              <el-button type="primary" icon="document">Export Appliance Inventory</el-button>
            </a>
          </article>
        </div>
      </div>
    </div>
  </div>
</template>
<!--
<template>

  <div id="appliances">
    <div class="tile is-ancestor">
      <div class="tile is-vertical is-8">
        <div class="tile">
          <div class="tile is-parent">
            <article class="tile is-child box">
              <div class="notification is-blue">
                <p class="subtitle">Appliances Model Distribution</p>
              </div>
              <app-model-pie title="Total Appliances" :data='modelDist' :drilldown='modelDrilldown' ></app-model-pie>
            </article>
          </div>

          <div class="tile is-parent">
            <article class="tile is-child box">
              <div class="notification is-blue">
                <p class="subtitle">Appliances Assigned To Teams</p>
              </div>
              <app-model-pie title="Assigned Appliances" :data='teamDist' :drilldown='teamDrilldown'></app-model-pie>
            </article>
          </div>

          <div class="tile is-parent">
            <article class="tile is-child box">
              <div class="notification is-blue">
                <p class="subtitle">Appliances Requsts In Next PI </p>
              </div>
              <app-model-pie title="Total" :data="appReqDist" :drilldown="appReqDrilldown"></app-model-pie>
            </article>
          </div>
        </div>

        <div @click="showSide=!showSide" class="tile is-parent">
          <article class="tile is-child box">
            <div class="notification is-blue">
              <p class="subtitle">Appliances List</p>
            </div>
            <app-list></app-list>
          </article>
        </div>
      </div>
      <div class="tile is-parent is-4">
        <article class="tile is-child box">
          <div class="content">
            <p class="title">Appliances Requests
              <a class="animated fadeIn"><i class="fa fa-plus-circle fa-lg" aria-hidden="true" @click="add" v-if="!addFlag"></i></a> 
            </p>
            <p class="subtitle">From scrum teams
            </p>
  
            <div class="box animated fadeIn" id="appliance-req" v-if="addFlag">
              <div class="notification">
                <el-select v-model="value7" placeholder="Select a team">
                  <el-option-group v-for="group in options3" :label="group.label" v-bind:key="group.label">
                    <el-option v-for="item in group.options" :label="item.label" :value="item.value" v-bind:key="item.value"> 
                    </el-option>
                  </el-option-group>
                </el-select>
              </div>

              <div class="notification">
                <el-radio class="radio" v-model="radio" label="1">5220</el-radio>
                <el-radio class="radio" v-model="radio" label="2">5230</el-radio>
                <el-radio class="radio" v-model="radio" label="3">5330</el-radio>
                <el-radio class="radio" v-model="radio" label="4">5340</el-radio>
              </div>

              <div class="notification">
                <div class="columns">
                  <div class="column">
                    <el-input-number v-model="num" @change="" :min="1"></el-input-number>
                  </div>
                  <div class="column">
                    <a class="button is-primary">Left: {{100 - num}}</a>
                  </div>
                </div>
              </div>

              <div class="notification">
                <el-input type="textarea"></el-input>
              </div>

              <a class="button is-primary" @click="save">Save</a>
              <a class="button is-primary" @click="cancel">Cancel</a>
            </div>

            <div class="content">
              <app-req-list :reqs = reqs ></app-req-list>
            </div>
          </div>
        </article>
      </div>
    </div>
  </div>
</template>

<script>
  import { AppList, AppModelPie, AppReqList } from './components/'
  import moment from 'moment'
  import _ from 'lodash'

  export default {
    data () {
      return {
        modelDist: [
          {name: '5220', y: 100, drilldown: '5220'},
          {name: '5330', y: 100},
          {name: '5230', y: 100},
          {name: '5330', y: 200},
          {name: '5240', y: 100},
          {name: '5340', y: 32}],
        modelDrilldown: [{
          name: '5220',
          id: '5220',
          data: [['Icebreaker', 10], ['Autobots', 11], ['Libra', 13], ['Goku', 9], ['Plan 9', 9]]
        }],
        teamDist: [
          {name: 'Icebreaker', y: 11, drilldown: 'Icebreaker'},
          {name: 'Autobots', y: 13},
          {name: 'Libra', y: 15},
          {name: 'Goku', y: 21},
          {name: 'Plan 9', y: 18}],
        teamDrilldown: [{
          name: 'Icebreaker',
          id: 'Icebreaker',
          data: [['5230', 10], ['5240', 11], ['5330', 13], ['5430', 9], ['5220', 9]]
        }],
        appReqDist: [
          {name: 'Icebreaker', y: 15, drilldown: 'Icebreaker'},
          {name: 'Autobots', y: 7},
          {name: 'Libra', y: 6},
          {name: 'Goku', y: 8},
          {name: 'Plan 9', y: 10}],
        appReqDrilldown: [{
          name: 'Icebreaker',
          id: 'Icebreaker',
          data: [['5230', 10], ['5240', 11], ['5330', 13], ['5430', 9], ['5220', 9]]
        }],
        showSide: false,
        radio: '1',
        addFlag: false,
        num: 1,
        options3: [{
          label: 'MTV',
          options: [{
            value: 'autobots',
            label: 'autobots'
          }, {
            value: 'pegrine',
            label: 'pegrine'
          }]
        }, {
          label: 'CD',
          options: [{
            value: 'icebreaker',
            label: 'icebreaker'
          }, {
            value: 'goku',
            label: 'goku'
          }]
        }],
        value7: '',
        reqs: _.sortBy([{
          status: _.shuffle(['Approved', 'Pending'])[0],
          assignedApps: [],
          team: 'Icebreaker',
          model: '5220',
          description: 'we need some machines',
          quantity: 1,
          created: moment()
        }, {
          status: _.shuffle(['Pending'])[0],
          assignedApps: [],
          team: 'Icebreaker',
          model: '5230',
          description: 'we need some machines',
          quantity: 1,
          created: moment()
        }, {
          status: _.shuffle(['Pending'])[0],
          assignedApps: [],
          team: 'Icebreaker',
          model: '5320',
          description: 'we need some machines',
          quantity: 1,
          created: moment()
        }], 'status')
      }
    },
    components: {
      AppList,
      AppModelPie,
      AppReqList
    },

    methods: {
      add () {
        this.addFlag = true
      },
      cancel () {
        this.addFlag = false
        this.num = 1
        this.radio = '1'
      },
      save () {
        this.addFlag = false
        this.num = 1
        this.radio = '1'
        this.reqs.push({
          status: 'Pending',
          assignedApps: [],
          team: 'Autobots',
          model: '5220',
          description: 'we need some machines',
          quantity: 1,
          created: moment()
        })
      }
    }
  }

</script>

<style>
  .is-blue {
    background-color: #4DD0E1;
    color: white
  }
</style>
-->
