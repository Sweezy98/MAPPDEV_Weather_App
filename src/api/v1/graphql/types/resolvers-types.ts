import { GraphQLResolveInfo } from 'graphql';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type RequireFields<T, K extends keyof T> = Omit<T, K> & { [P in K]-?: NonNullable<T[P]> };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: string;
  String: string;
  Boolean: boolean;
  Int: number;
  Float: number;
};

/** returned data after login */
export type AuthData = {
  __typename?: 'AuthData';
  /** Email of the user */
  email?: Maybe<Scalars['String']>;
  /** Name of the user */
  name?: Maybe<Scalars['String']>;
  /** session id */
  session?: Maybe<Scalars['String']>;
  /** access and refresh token */
  tokens?: Maybe<AuthTokens>;
  /** Unique identifier from Database for the user */
  userId?: Maybe<Scalars['ID']>;
};

/** access and refresh token */
export type AuthTokens = {
  __typename?: 'AuthTokens';
  /**
   * access token
   * valid for 15 minutes
   */
  accessToken?: Maybe<Scalars['String']>;
  /**
   * refresh token
   * valid for 30 days
   * gets refreshed on every renewToken call
   */
  refreshToken?: Maybe<Scalars['String']>;
};

/** Favourite Object */
export type Favourite = {
  __typename?: 'Favourite';
  /** Unique identifier from Database for the favourite */
  _id?: Maybe<Scalars['ID']>;
  /** latitude of the favourite */
  lat?: Maybe<Scalars['Float']>;
  /** longitude of the favourite */
  lon?: Maybe<Scalars['Float']>;
  /** Name of the favourite */
  name?: Maybe<Scalars['String']>;
};

/** Favorite data needed to create a new favourite */
export type FavouriteInput = {
  /** latitude of the favourite */
  lat: Scalars['Float'];
  /** longitude of the favourite */
  lon: Scalars['Float'];
  /** Name of the favourite */
  name: Scalars['String'];
};

export type Log = {
  __typename?: 'Log';
  /** level */
  level?: Maybe<Scalars['String']>;
  /** message */
  message?: Maybe<Scalars['String']>;
  /** timestamp */
  timestamp?: Maybe<Scalars['String']>;
  /** trace (sender of the log) */
  trace?: Maybe<Scalars['String']>;
};

export type Mutation = {
  __typename?: 'Mutation';
  /**
   * add a favourite to the current user
   * returns the new favourite list
   */
  addFavourite?: Maybe<Array<Maybe<Favourite>>>;
  /**
   * change user name
   * returns true if name was changed
   */
  changeName?: Maybe<Scalars['Boolean']>;
  /**
   * change password
   * returns true if password was changed
   */
  changePassword?: Maybe<Scalars['Boolean']>;
  /** register a new user */
  createUser?: Maybe<User>;
  /**
   * remove a favourite from the current user
   * returns the new favourite list
   */
  removeFavourite?: Maybe<Array<Maybe<Favourite>>>;
};


export type MutationAddFavouriteArgs = {
  favourite: FavouriteInput;
};


export type MutationChangeNameArgs = {
  name: Scalars['String'];
};


export type MutationChangePasswordArgs = {
  newPassword: Scalars['String'];
  oldPassword: Scalars['String'];
};


export type MutationCreateUserArgs = {
  user: UserInput;
};


export type MutationRemoveFavouriteArgs = {
  id: Scalars['ID'];
};

export type Query = {
  __typename?: 'Query';
  /**
   * forgot password
   * returns true if email was sent
   * returns also true if email was not found (security)
   */
  forgotPassword?: Maybe<Scalars['Boolean']>;
  /** getLogs */
  getLogs?: Maybe<Array<Maybe<Log>>>;
  /** Query weather data for a specific location */
  getWeatherData?: Maybe<WeatherData>;
  login?: Maybe<AuthData>;
  /**
   * logout
   * returns true if logout was successful
   */
  logout?: Maybe<Scalars['Boolean']>;
  /** returns a new accessToken and refreshes the refreshToken */
  renewToken?: Maybe<AuthTokens>;
  /** Say hello */
  sayHello?: Maybe<Scalars['String']>;
  /** get a list of all sessions */
  sessions?: Maybe<Array<Maybe<Session>>>;
  /** get a single user */
  user?: Maybe<User>;
  /** get a list of all favourites of the current user */
  userFavourites?: Maybe<Array<Maybe<Favourite>>>;
  /** get a list of all sessions of the current user */
  userSessions?: Maybe<Array<Maybe<Session>>>;
  /** get a list of all users */
  users?: Maybe<Array<Maybe<User>>>;
};


export type QueryForgotPasswordArgs = {
  email: Scalars['String'];
};


export type QueryGetLogsArgs = {
  limit?: InputMaybe<Scalars['Int']>;
  offset?: InputMaybe<Scalars['Int']>;
};


export type QueryGetWeatherDataArgs = {
  lat: Scalars['Float'];
  lon: Scalars['Float'];
};


export type QueryLoginArgs = {
  email: Scalars['String'];
  password: Scalars['String'];
};


export type QueryLogoutArgs = {
  session: Scalars['String'];
};


export type QueryRenewTokenArgs = {
  refreshToken: Scalars['String'];
};


export type QuerySayHelloArgs = {
  name: Scalars['String'];
};


export type QueryUserArgs = {
  _id: Scalars['ID'];
};

/** Session Object */
export type Session = {
  __typename?: 'Session';
  /** Unique identifier from Database for the session */
  _id?: Maybe<Scalars['ID']>;
  /** User of the session */
  user?: Maybe<User>;
  /** valid until */
  validUntil?: Maybe<Scalars['String']>;
};

/** Temperature, with units Celsius and Fahrenheit */
export type Temperature = {
  __typename?: 'Temperature';
  /** Temperature. Unit Celsius */
  c?: Maybe<Scalars['Int']>;
  /** Temperature. Unit Fahrenheit */
  f?: Maybe<Scalars['Int']>;
};

/** Temperature data */
export type TemperatureData = {
  __typename?: 'TemperatureData';
  /** Current temperature. Null if not the current day. */
  cur?: Maybe<Temperature>;
  /** Temperature. This temperature parameter accounts for the human perception of weather. */
  feel?: Maybe<Temperature>;
  /** Maximum temperature at the moment. This is deviation from current temp that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). */
  max?: Maybe<Temperature>;
  /** Minimum temperature at the moment. This is deviation from current temp that is possible for large cities and megalopolises geographically expanded (use these parameter optionally). */
  min?: Maybe<Temperature>;
};

/** User Object */
export type User = {
  __typename?: 'User';
  /** Unique identifier from Database for the user */
  _id?: Maybe<Scalars['ID']>;
  /** Email of the user */
  email: Scalars['String'];
  /** Name of the user */
  name?: Maybe<Scalars['String']>;
  /** Account verified */
  verified?: Maybe<Scalars['Boolean']>;
};

/** Userdata needed to create a new user */
export type UserInput = {
  /** Email of the user */
  email: Scalars['String'];
  /** Name of the user */
  name: Scalars['String'];
  /**
   * Password of the user
   * Stored as a hash
   */
  password: Scalars['String'];
};

/** Weather data for a specific location */
export type WeatherData = {
  __typename?: 'WeatherData';
  /** id of favourite location */
  favouriteId?: Maybe<Scalars['String']>;
  /** True if user logged in and location is favourite, otherwise false */
  isFavourite?: Maybe<Scalars['Boolean']>;
  /** Location name */
  name?: Maybe<Scalars['String']>;
  /** Array of weather data */
  weather?: Maybe<Array<Maybe<WeatherDataForecast>>>;
};

/** Weather data for a specific day */
export type WeatherDataForecast = {
  __typename?: 'WeatherDataForecast';
  /** Weather code */
  code?: Maybe<Scalars['Int']>;
  /** Date of the forecast */
  date?: Maybe<Scalars['String']>;
  /** Description of the weather */
  description?: Maybe<Scalars['String']>;
  /** Weather Details */
  details?: Maybe<WeatherDetails>;
  /** Weather icon */
  icon?: Maybe<Scalars['String']>;
  /** Temperature data */
  temps?: Maybe<TemperatureData>;
  /** Day of the week */
  weekday?: Maybe<Weekday>;
};

/** Weather Details */
export type WeatherDetails = {
  __typename?: 'WeatherDetails';
  /** Humidity, % */
  humidity?: Maybe<Scalars['Float']>;
  /** Rain volume for the last 3 hours */
  precipitation?: Maybe<Scalars['Float']>;
  /** Atmospheric pressure on the sea level, hPa */
  pressure?: Maybe<Scalars['Float']>;
  /** Visibility, meter */
  visibility?: Maybe<Scalars['Float']>;
  /** Wind data */
  wind?: Maybe<Wind>;
};

/** Day of the week */
export type Weekday = {
  __typename?: 'Weekday';
  /** Day of the week in long format */
  long?: Maybe<Scalars['String']>;
  /** Day of the week in short format */
  short?: Maybe<Scalars['String']>;
};

/** Wind data */
export type Wind = {
  __typename?: 'Wind';
  /** Wind direction, degrees (meteorological) */
  deg?: Maybe<Scalars['Int']>;
  /** Wind speed. Unit Default: meter/sec */
  speed?: Maybe<Scalars['Float']>;
};

export type WithIndex<TObject> = TObject & Record<string, any>;
export type ResolversObject<TObject> = WithIndex<TObject>;

export type ResolverTypeWrapper<T> = Promise<T> | T;


export type ResolverWithResolve<TResult, TParent, TContext, TArgs> = {
  resolve: ResolverFn<TResult, TParent, TContext, TArgs>;
};
export type Resolver<TResult, TParent = {}, TContext = {}, TArgs = {}> = ResolverFn<TResult, TParent, TContext, TArgs> | ResolverWithResolve<TResult, TParent, TContext, TArgs>;

export type ResolverFn<TResult, TParent, TContext, TArgs> = (
  parent: TParent,
  args: TArgs,
  context: TContext,
  info: GraphQLResolveInfo
) => Promise<TResult> | TResult;

export type SubscriptionSubscribeFn<TResult, TParent, TContext, TArgs> = (
  parent: TParent,
  args: TArgs,
  context: TContext,
  info: GraphQLResolveInfo
) => AsyncIterable<TResult> | Promise<AsyncIterable<TResult>>;

export type SubscriptionResolveFn<TResult, TParent, TContext, TArgs> = (
  parent: TParent,
  args: TArgs,
  context: TContext,
  info: GraphQLResolveInfo
) => TResult | Promise<TResult>;

export interface SubscriptionSubscriberObject<TResult, TKey extends string, TParent, TContext, TArgs> {
  subscribe: SubscriptionSubscribeFn<{ [key in TKey]: TResult }, TParent, TContext, TArgs>;
  resolve?: SubscriptionResolveFn<TResult, { [key in TKey]: TResult }, TContext, TArgs>;
}

export interface SubscriptionResolverObject<TResult, TParent, TContext, TArgs> {
  subscribe: SubscriptionSubscribeFn<any, TParent, TContext, TArgs>;
  resolve: SubscriptionResolveFn<TResult, any, TContext, TArgs>;
}

export type SubscriptionObject<TResult, TKey extends string, TParent, TContext, TArgs> =
  | SubscriptionSubscriberObject<TResult, TKey, TParent, TContext, TArgs>
  | SubscriptionResolverObject<TResult, TParent, TContext, TArgs>;

export type SubscriptionResolver<TResult, TKey extends string, TParent = {}, TContext = {}, TArgs = {}> =
  | ((...args: any[]) => SubscriptionObject<TResult, TKey, TParent, TContext, TArgs>)
  | SubscriptionObject<TResult, TKey, TParent, TContext, TArgs>;

export type TypeResolveFn<TTypes, TParent = {}, TContext = {}> = (
  parent: TParent,
  context: TContext,
  info: GraphQLResolveInfo
) => Maybe<TTypes> | Promise<Maybe<TTypes>>;

export type IsTypeOfResolverFn<T = {}, TContext = {}> = (obj: T, context: TContext, info: GraphQLResolveInfo) => boolean | Promise<boolean>;

export type NextResolverFn<T> = () => Promise<T>;

export type DirectiveResolverFn<TResult = {}, TParent = {}, TContext = {}, TArgs = {}> = (
  next: NextResolverFn<TResult>,
  parent: TParent,
  args: TArgs,
  context: TContext,
  info: GraphQLResolveInfo
) => TResult | Promise<TResult>;

/** Mapping between all available schema types and the resolvers types */
export type ResolversTypes = ResolversObject<{
  AuthData: ResolverTypeWrapper<AuthData>;
  AuthTokens: ResolverTypeWrapper<AuthTokens>;
  Boolean: ResolverTypeWrapper<Scalars['Boolean']>;
  Favourite: ResolverTypeWrapper<Favourite>;
  FavouriteInput: FavouriteInput;
  Float: ResolverTypeWrapper<Scalars['Float']>;
  ID: ResolverTypeWrapper<Scalars['ID']>;
  Int: ResolverTypeWrapper<Scalars['Int']>;
  Log: ResolverTypeWrapper<Log>;
  Mutation: ResolverTypeWrapper<{}>;
  Query: ResolverTypeWrapper<{}>;
  Session: ResolverTypeWrapper<Session>;
  String: ResolverTypeWrapper<Scalars['String']>;
  Temperature: ResolverTypeWrapper<Temperature>;
  TemperatureData: ResolverTypeWrapper<TemperatureData>;
  User: ResolverTypeWrapper<User>;
  UserInput: UserInput;
  WeatherData: ResolverTypeWrapper<WeatherData>;
  WeatherDataForecast: ResolverTypeWrapper<WeatherDataForecast>;
  WeatherDetails: ResolverTypeWrapper<WeatherDetails>;
  Weekday: ResolverTypeWrapper<Weekday>;
  Wind: ResolverTypeWrapper<Wind>;
}>;

/** Mapping between all available schema types and the resolvers parents */
export type ResolversParentTypes = ResolversObject<{
  AuthData: AuthData;
  AuthTokens: AuthTokens;
  Boolean: Scalars['Boolean'];
  Favourite: Favourite;
  FavouriteInput: FavouriteInput;
  Float: Scalars['Float'];
  ID: Scalars['ID'];
  Int: Scalars['Int'];
  Log: Log;
  Mutation: {};
  Query: {};
  Session: Session;
  String: Scalars['String'];
  Temperature: Temperature;
  TemperatureData: TemperatureData;
  User: User;
  UserInput: UserInput;
  WeatherData: WeatherData;
  WeatherDataForecast: WeatherDataForecast;
  WeatherDetails: WeatherDetails;
  Weekday: Weekday;
  Wind: Wind;
}>;

export type AuthDataResolvers<ContextType = any, ParentType extends ResolversParentTypes['AuthData'] = ResolversParentTypes['AuthData']> = ResolversObject<{
  email?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  session?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  tokens?: Resolver<Maybe<ResolversTypes['AuthTokens']>, ParentType, ContextType>;
  userId?: Resolver<Maybe<ResolversTypes['ID']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type AuthTokensResolvers<ContextType = any, ParentType extends ResolversParentTypes['AuthTokens'] = ResolversParentTypes['AuthTokens']> = ResolversObject<{
  accessToken?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  refreshToken?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type FavouriteResolvers<ContextType = any, ParentType extends ResolversParentTypes['Favourite'] = ResolversParentTypes['Favourite']> = ResolversObject<{
  _id?: Resolver<Maybe<ResolversTypes['ID']>, ParentType, ContextType>;
  lat?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  lon?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type LogResolvers<ContextType = any, ParentType extends ResolversParentTypes['Log'] = ResolversParentTypes['Log']> = ResolversObject<{
  level?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  message?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  timestamp?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  trace?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type MutationResolvers<ContextType = any, ParentType extends ResolversParentTypes['Mutation'] = ResolversParentTypes['Mutation']> = ResolversObject<{
  addFavourite?: Resolver<Maybe<Array<Maybe<ResolversTypes['Favourite']>>>, ParentType, ContextType, RequireFields<MutationAddFavouriteArgs, 'favourite'>>;
  changeName?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType, RequireFields<MutationChangeNameArgs, 'name'>>;
  changePassword?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType, RequireFields<MutationChangePasswordArgs, 'newPassword' | 'oldPassword'>>;
  createUser?: Resolver<Maybe<ResolversTypes['User']>, ParentType, ContextType, RequireFields<MutationCreateUserArgs, 'user'>>;
  removeFavourite?: Resolver<Maybe<Array<Maybe<ResolversTypes['Favourite']>>>, ParentType, ContextType, RequireFields<MutationRemoveFavouriteArgs, 'id'>>;
}>;

export type QueryResolvers<ContextType = any, ParentType extends ResolversParentTypes['Query'] = ResolversParentTypes['Query']> = ResolversObject<{
  forgotPassword?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType, RequireFields<QueryForgotPasswordArgs, 'email'>>;
  getLogs?: Resolver<Maybe<Array<Maybe<ResolversTypes['Log']>>>, ParentType, ContextType, Partial<QueryGetLogsArgs>>;
  getWeatherData?: Resolver<Maybe<ResolversTypes['WeatherData']>, ParentType, ContextType, RequireFields<QueryGetWeatherDataArgs, 'lat' | 'lon'>>;
  login?: Resolver<Maybe<ResolversTypes['AuthData']>, ParentType, ContextType, RequireFields<QueryLoginArgs, 'email' | 'password'>>;
  logout?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType, RequireFields<QueryLogoutArgs, 'session'>>;
  renewToken?: Resolver<Maybe<ResolversTypes['AuthTokens']>, ParentType, ContextType, RequireFields<QueryRenewTokenArgs, 'refreshToken'>>;
  sayHello?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType, RequireFields<QuerySayHelloArgs, 'name'>>;
  sessions?: Resolver<Maybe<Array<Maybe<ResolversTypes['Session']>>>, ParentType, ContextType>;
  user?: Resolver<Maybe<ResolversTypes['User']>, ParentType, ContextType, RequireFields<QueryUserArgs, '_id'>>;
  userFavourites?: Resolver<Maybe<Array<Maybe<ResolversTypes['Favourite']>>>, ParentType, ContextType>;
  userSessions?: Resolver<Maybe<Array<Maybe<ResolversTypes['Session']>>>, ParentType, ContextType>;
  users?: Resolver<Maybe<Array<Maybe<ResolversTypes['User']>>>, ParentType, ContextType>;
}>;

export type SessionResolvers<ContextType = any, ParentType extends ResolversParentTypes['Session'] = ResolversParentTypes['Session']> = ResolversObject<{
  _id?: Resolver<Maybe<ResolversTypes['ID']>, ParentType, ContextType>;
  user?: Resolver<Maybe<ResolversTypes['User']>, ParentType, ContextType>;
  validUntil?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type TemperatureResolvers<ContextType = any, ParentType extends ResolversParentTypes['Temperature'] = ResolversParentTypes['Temperature']> = ResolversObject<{
  c?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  f?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type TemperatureDataResolvers<ContextType = any, ParentType extends ResolversParentTypes['TemperatureData'] = ResolversParentTypes['TemperatureData']> = ResolversObject<{
  cur?: Resolver<Maybe<ResolversTypes['Temperature']>, ParentType, ContextType>;
  feel?: Resolver<Maybe<ResolversTypes['Temperature']>, ParentType, ContextType>;
  max?: Resolver<Maybe<ResolversTypes['Temperature']>, ParentType, ContextType>;
  min?: Resolver<Maybe<ResolversTypes['Temperature']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type UserResolvers<ContextType = any, ParentType extends ResolversParentTypes['User'] = ResolversParentTypes['User']> = ResolversObject<{
  _id?: Resolver<Maybe<ResolversTypes['ID']>, ParentType, ContextType>;
  email?: Resolver<ResolversTypes['String'], ParentType, ContextType>;
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  verified?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type WeatherDataResolvers<ContextType = any, ParentType extends ResolversParentTypes['WeatherData'] = ResolversParentTypes['WeatherData']> = ResolversObject<{
  favouriteId?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  isFavourite?: Resolver<Maybe<ResolversTypes['Boolean']>, ParentType, ContextType>;
  name?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  weather?: Resolver<Maybe<Array<Maybe<ResolversTypes['WeatherDataForecast']>>>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type WeatherDataForecastResolvers<ContextType = any, ParentType extends ResolversParentTypes['WeatherDataForecast'] = ResolversParentTypes['WeatherDataForecast']> = ResolversObject<{
  code?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  date?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  description?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  details?: Resolver<Maybe<ResolversTypes['WeatherDetails']>, ParentType, ContextType>;
  icon?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  temps?: Resolver<Maybe<ResolversTypes['TemperatureData']>, ParentType, ContextType>;
  weekday?: Resolver<Maybe<ResolversTypes['Weekday']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type WeatherDetailsResolvers<ContextType = any, ParentType extends ResolversParentTypes['WeatherDetails'] = ResolversParentTypes['WeatherDetails']> = ResolversObject<{
  humidity?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  precipitation?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  pressure?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  visibility?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  wind?: Resolver<Maybe<ResolversTypes['Wind']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type WeekdayResolvers<ContextType = any, ParentType extends ResolversParentTypes['Weekday'] = ResolversParentTypes['Weekday']> = ResolversObject<{
  long?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  short?: Resolver<Maybe<ResolversTypes['String']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type WindResolvers<ContextType = any, ParentType extends ResolversParentTypes['Wind'] = ResolversParentTypes['Wind']> = ResolversObject<{
  deg?: Resolver<Maybe<ResolversTypes['Int']>, ParentType, ContextType>;
  speed?: Resolver<Maybe<ResolversTypes['Float']>, ParentType, ContextType>;
  __isTypeOf?: IsTypeOfResolverFn<ParentType, ContextType>;
}>;

export type Resolvers<ContextType = any> = ResolversObject<{
  AuthData?: AuthDataResolvers<ContextType>;
  AuthTokens?: AuthTokensResolvers<ContextType>;
  Favourite?: FavouriteResolvers<ContextType>;
  Log?: LogResolvers<ContextType>;
  Mutation?: MutationResolvers<ContextType>;
  Query?: QueryResolvers<ContextType>;
  Session?: SessionResolvers<ContextType>;
  Temperature?: TemperatureResolvers<ContextType>;
  TemperatureData?: TemperatureDataResolvers<ContextType>;
  User?: UserResolvers<ContextType>;
  WeatherData?: WeatherDataResolvers<ContextType>;
  WeatherDataForecast?: WeatherDataForecastResolvers<ContextType>;
  WeatherDetails?: WeatherDetailsResolvers<ContextType>;
  Weekday?: WeekdayResolvers<ContextType>;
  Wind?: WindResolvers<ContextType>;
}>;

